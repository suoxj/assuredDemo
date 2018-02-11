package com.haier.uhome.uplus.test;

import com.haier.uhome.uplus.model.Activity;
import com.haier.uhome.uplus.model.Prize;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.internal.multipart.MultiPartSpecificationImpl;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.*;

import static io.restassured.RestAssured.*;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.Matchers.*;


/**
 * Created by suoxiaojing on 2017/7/28.
 */

public class Omsva {

    Activity activity = new Activity();
    Prize prize = new Prize();

    @BeforeTest
    public void setup() {
        baseURI = "http://210.51.17.150";
//        port = 80;
        basePath = "omsva";

    }

    @Test
    public void test01_create_Activity_success() {

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(d);

        Random random = new Random();
        Integer index = random.nextInt(1000);
        activity.setActivityUuid("application_increase_" + index + "_" + dateNowStr);
        activity.setActivityName("U+APP拉新活动");
        activity.setActivityRemark("负责人袁章书，创建于2017年8月7日-8月21日");
        activity.setBeginTime("2017-07-01 00:00:00");
        activity.setEndTime("2017-08-31 23:59:59");
        activity.setScope("1");

        String id =
        given().log().all().contentType("application/json; charset=UTF-8")
                .body(activity).
        when().
                post("/activity/create").
        then().log().all().statusCode(200).
                body("retCode", equalTo("00000")).
                extract().path("data.activityId");

        activity.setId(Integer.parseInt(id));
    }

    @Test
    public void test02_queryUuidIsExist_true() {
        given().log().all().contentType(ContentType.JSON).
        when().
        get("activity/query/uuid/{activityUuid}", activity.getActivityUuid()).then().body("data.isExist", is(true));
    }

    @Test
    public void test03_queryUuidIsExist_false() {
        given().log().all().contentType(ContentType.JSON).
                when().
                get("activity/query/uuid/{activityUuid}", System.currentTimeMillis()).then().body("data.isExist", is(false));
    }

    @Test
    public void test04_queryActivityDetail() {
        given().log().all().contentType(ContentType.JSON).
                when().
                post("activity/query/{activityId}", activity.getId()).
                then().
                log().all().
                body("retCode", equalTo("00000")).
                body("data.activity.id", equalTo(activity.getId())).
                body("data.activity.activityName", equalTo(activity.getActivityName())).
                body("data.activity.activityRemark", equalTo(activity.getActivityRemark()));
    }

    @Test
    public void test05_modifyActivity() {
       activity.setActivityName("测试修改activity");
       activity.setActivityRemark("测试修改remark");
       activity.setEndTime("2017-09-31 23:59:59");

       given().log().all().contentType("application/json; charset=UTF-8")
                .body(activity).
                when().post("/activity/update").
                then().
                log().all().
                body("retCode", equalTo("00000"));

       test04_queryActivityDetail();
    }

    @Test
    public void test06_queryAllActivity() {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("index", "0");
        jsonMap.put("count", "4");
        given().log().all().contentType("application/json; charset=UTF-8").
                body(jsonMap)
                .post("/activity/query")
                .then()
                .log().all()
                .body("data.items.id", hasItem(activity.getId()));
    }

    @Test
    public void test07_uploadImage() {
        MultiPartSpecificationImpl multiPartSpecification = new MultiPartSpecificationImpl();
        multiPartSpecification.setFileName("/Users/suoxiaojing/20170623143932.png");
//        multiPartSpecification.setFileName("dfdfdfdfdfd");

        multiPartSpecification.setMimeType("image/png");
        String type = multiPartSpecification.getFileName();
        System.out.println(type);
        String url;
        url = given().log().all()
//                .multiPart("file", new File("/Users/suoxiaojing/20170623143932.png"))
                .multiPart(new MultiPartSpecBuilder("Test-Content-In-File".getBytes()).
                        fileName("/Users/suoxiaojing/20170623143932.png").
                        controlName("file").
                        mimeType("image/png").
                        build())
                .post("/prize/upload/img")
                .then()
                .log().all()
                .body("retCode", equalTo("00000"))
                .extract()
                .path("data.url");
        prize.setPrizeImg(url);
    }

    @Test
    public void test08_createPrize() {
        prize.setActivityId(activity.getId());
        prize.setPrizeName("电影票");
        prize.setPrizeNumber(10);
        prize.setPrizeQuota("100");
        prize.setPrizeRemark("备注");
        prize.setPrizeType(1);
        prize.setRate(2);

        given().contentType(ContentType.JSON).log().all()
                .body(prize)
                .when()
                .post("/prize/create")
                .then().log().all()
                .body("retCode", equalTo("00000"));
    }

    @Test
    public void test09_queryPrize() {
        Map<String,Integer> json = new HashMap<>();
        json.put("activityId", activity.getId());
        Integer id =
        given().contentType(ContentType.JSON).log().all()
                .body(json)
                .when()
                .post("/prize/query")
                .then().log().all()
                .body("retCode", equalTo("00000"))
                .extract()
                .path("data.items.find{ it.prizeRemark='备注'}.id");

        prize.setId(id);
    }

    @Test
    public void test09_queryPrize1() {
        Map<String,Integer> json = new HashMap<>();
        json.put("activityId", activity.getId());
        String response =
                given().contentType(ContentType.JSON).log().all()
                        .body(json)
                        .when()
                        .post("/prize/query")
                        .asString();

        List<Integer> ids = from(response).getList("data.items.id");
        System.out.println(ids);

    }

    @Test
    public void test10_modifyPrize() {
        prize.setPrizeName("修改电影票");
        prize.setPrizeNumber(20);
        prize.setPrizeQuota("10");
        prize.setPrizeRemark("修改备注");
        prize.setPrizeType(1);
        prize.setRate(1);
        given().contentType(ContentType.JSON)
                .log().all()
                .body(prize)
                .when()
                .post("/prize/update")
                .then().log().all()
                .body("retCode", equalTo("00000"));
    }

    @Test
    public void test11_queryPrizeDetail() {
        given().contentType(ContentType.JSON)
                .log().all()
                .when()
                .post("/prize/query/{prizeId}", prize.getId())
                .then().log().all()
                .body("retCode", equalTo("00000"))
                .body("data.prize.activityId", equalTo(activity.getId()))
                .body("data.prize.prizeName", equalTo(prize.getPrizeName()))
                .body("data.prize.prizeRemark", equalTo(prize.getPrizeRemark()));
    }

    @Test
    public void test12_queryPrizeType() {
        given().contentType(ContentType.JSON)
                .log().all()
                .when()
                .get("/prize/type")
                .then().log().all()
                .body("data.items.id", hasItem(1));
    }

    @Test
    public void test13_queryTestScope() {
        given().contentType(ContentType.JSON)
                .log().all()
                .get("/activity/scope")
                .then().log().all()
                .body("data.items.id", hasItem(1));
    }

    @Test
    public void test14_lottery() {
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("userPhone", "18966929677");
        jsonMap.put("activityUuid", activity.getActivityUuid());
        jsonMap.put("uhomeAccessToken", "TGT13X90N9NRL9U2UWDC7S17L30800");
        jsonMap.put("clientId", "6317256339615");
        given().contentType(ContentType.JSON)
                .log().all()
                .body(jsonMap)
                .post("/activity/lottery")
                .then().log().all()
                .body("retCode", equalTo("20000"))
                .body("retInfo", equalTo("抽奖成功"));
    }

    @Test
    public void test15_getLotteryUser() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNowStr = sdf.format(d);
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("activityId", activity.getId().toString());
        jsonMap.put("beginTime", "2017-07-11 00:00:00");
        jsonMap.put("endTime", dateNowStr);
        jsonMap.put("index", "0");
        jsonMap.put("count", "10");

        given().contentType(ContentType.JSON)
                .log().all()
                .body(jsonMap)
                .when()
                .post("/winning/query")
                .then().log().all()
                .body("data.items.userPhone", hasItem("18966929677"));

    }

    @Test
    public void test16_clearLottePool() {
        given().contentType(ContentType.JSON)
                .log().all()
                .when()
                .post("/activity/clear/{activityUuid}", activity.getActivityUuid())
                .then()
                .body("retCode", equalTo("00000"));
    }

    @Test
    public void test17_deletePrize() {
        given().contentType(ContentType.JSON)
                .log().all()
                .when()
                .post("/prize/delete/{prizeId}", prize.getId())
                .then()
                .body("retCode", equalTo("00000"));

        given().contentType(ContentType.JSON)
                .log().all()
                .when()
                .post("/prize/query/{prizeId}", prize.getId())
                .then().log().all()
                .body("data.prize", equalTo(null));

    }

    @Test
    public void test18_queryAllPrize() {
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("activityId", activity.getId().toString());

        given().contentType(ContentType.JSON)
                .body(jsonMap)
                .log().all()
                .when()
                .post("/prize/queryall")
                .then()
                .log().all()
                .body("data.items.id", hasItem(prize.getId()));
    }

    @Test
    public void test19_deleteActivity() {
        given().contentType(ContentType.JSON)
                .log().all()
                .when()
                .post("/activity/delete/{activityId}", activity.getId())
                .then().log().all()
                .body("retCode", equalTo("00000"));

        given().contentType(ContentType.JSON)
                .log().all()
                .when()
                .post("/activity/query/{activityId}", activity.getId())
                .then().log().all()
                .body("data.activity", equalTo(null));
    }
}
