package com.bdilab.dataflow.sql.generator;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.dto.jobdescription.TransformationDescription;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseJdbcUtils;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @author Zunjing Chen
 * @date 2021-12-10
 * <p>
 * 测试方案： 1. 每种类型（5种）都测 2. 每种类型不同操作选取代表性的测试 3. 每种类型转换数目为单个、多个
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TransformationSqlGeneratorTest {

  TransformationSqlGenerator transformationSqlGenerator;
  TransformationDescription transformationDescription;
  @Resource
  ClickHouseJdbcUtils clickHouseJdbcUtils;

  @Value("${clickhouse.http.url}")
  private String httpPrefix;


  @Before
  public void init() {
  }

  @Test
  public void testExpression() {
    expression();
    String sql = new TransformationSqlGenerator(transformationDescription).generateDataSourceSql();
    log.info(sql);
    clickHouseJdbcUtils.queryForList(sql);
  }

  private void expression() {
    String json = "{\n"
        + "  \"dataSource\":[\"dataflow.promotion_csv\"],\n"
        + "  \"limit\":2000,\n"
        + "  \"jobType\":\"transformation\",\n"
        + "  \"expressions\":[\n"
        + "    {\n"
        + "    \"newColumnName\":\"Married_new\", \n"
        + "    \"expression\":\"replaceAll(Married,'married','已婚')\",\n"
        + "    \"isWhatIf\":false \n"
        + "    }\n"
        + "  ]\n"
        + "}";
    transformationDescription = JSONObject.parseObject(json, TransformationDescription.class);
  }

  @Test
  public void testFindReplace() {
    findReplace();
    String sql = new TransformationSqlGenerator(transformationDescription).generateDataSourceSql();
    log.info(sql);
    clickHouseJdbcUtils.queryForList(sql);
  }

  private void findReplace() {
    String json = "{\n"
        + "  \"dataSource\":[\"dataflow.promotion_csv\"],\n"
        + "  \"limit\":2000,\n"
        + "  \"jobType\":\"transformation\",\n"
        + "  \"findReplaces\":[\n"
        + "    {\n"
        + "      \"searchInColumnName\":\"Married\" ,\n"
        + "      \"newColumnName\":\"Married_new\",\n"
        + "      \"searchReplaceMap\":{\n"
        + "      \t\"married\":\"已婚\",\n"
        + "        \"single\":\"单身\"\n"
        + "      }\n"
        + "    }\n"
        + "  ]\n"
        + "}";
    transformationDescription = JSONObject.parseObject(json, TransformationDescription.class);
  }

  @Test
  public void testBinarizer() {
    binarizer();
    String sql = new TransformationSqlGenerator(transformationDescription).generateDataSourceSql();
    log.info(sql);
    clickHouseJdbcUtils.queryForList(sql);
  }

  private void binarizer() {
    String json = "{\n"
        + "   \"dataSource\":[\"dataflow.promotion_csv\"],\n"
        + "  \"limit\":2000,\n"
        + "  \"jobType\":\"transformation\",\n"
        + "  \"binarizers\":[\n"
        + "    {\n"
        + "      \"newColumnName\":\"IsGoodCredit\",\n"
        + "      \"filter\":\"CreditScore>700\"\n"
        + "    }\n"
        + "  ]\n"
        + "}";
    transformationDescription = JSONObject.parseObject(json, TransformationDescription.class);

  }

  @Test
  public void testDataType() {
    String json = "{\n"
        + "  \"dataSource\":[\"dataflow.promotion_csv\"],\n"
        + "  \"limit\":2000,\n"
        + "  \"jobType\":\"transformation\",\n"
        + "  \"dataTypes\":[\n"
        + "    {\n"
        + "\t\t\t\"columnName\":\"LeasePrice\",\n"
        + "      \"newColumnName\":\"LeasePrice_Int\",\n"
        + "      \"dataType\":\"double\" \n"
        + "    }\n"
        + "  ]\n"
        + "}";
    transformationDescription = JSONObject.parseObject(json, TransformationDescription.class);
    String sql = new TransformationSqlGenerator(transformationDescription).generateDataSourceSql();
    log.info(sql);
    clickHouseJdbcUtils.queryForList(sql);
  }

  @Test
  public void testCustomBinning() {
    customBinning();
    String sql = new TransformationSqlGenerator(transformationDescription).generateDataSourceSql();
    log.info(sql);
    clickHouseJdbcUtils.queryForList(sql);
  }

  private void customBinning() {
    String json = "{\n"
        + "  \"dataSource\":[\"dataflow.promotion_csv\"],\n"
        + "  \"limit\":2000,\n"
        + "  \"jobType\":\"transformation\",\n"
        + "  \"customBinnings\":[\n"
        + "    {\n"
        + "\t\t\t\"newColumnName\":\"Credit\",\n"
        + "      \"defaultBin\":\"' '\",\n"
        + "      \"isNumeric\": true,\n"
        + "      \"bins\":[\n"
        + "        {\n"
        + "          \"binValue\":\"'信用极好'\",\n"
        + "          \"filter\":\"CreditScore >= 800\"\n"
        + "        },\n"
        + "        {\n"
        + "          \"binValue\":\"'信用好'\",\n"
        + "          \"filter\":\"CreditScore < 800 And CreditScore >= 600 \"\n"
        + "        },\n"
        + "        {\n"
        + "          \"binValue\":\"'信用中等'\",\n"
        + "          \"filter\":\"CreditScore < 600 And CreditScore >= 400 \"\n"
        + "        },\n"
        + "         {\n"
        + "          \"binValue\":\"'信用差'\",\n"
        + "          \"filter\":\"CreditScore < 400\"\n"
        + "        }\n"
        + "      ]\n"
        + "    }\n"
        + "  ]\n"
        + "}";
    transformationDescription = JSONObject.parseObject(json, TransformationDescription.class);

  }

}

