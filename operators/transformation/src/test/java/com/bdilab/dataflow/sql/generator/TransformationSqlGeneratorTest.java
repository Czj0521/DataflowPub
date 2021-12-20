package com.bdilab.dataflow.sql.generator;

import com.bdilab.dataflow.dto.Bin;
import com.bdilab.dataflow.dto.Binarizer;
import com.bdilab.dataflow.dto.CustomBinning;
import com.bdilab.dataflow.dto.DataType;
import com.bdilab.dataflow.dto.Expression;
import com.bdilab.dataflow.dto.FindReplace;
import com.bdilab.dataflow.dto.jobdescription.TransformationDescription;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseJdbcUtils;
import com.google.common.collect.ImmutableList;
import java.util.List;
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

  /**
   * initialize .
   */
  @Before
  public void init() {
    transformationDescription = new TransformationDescription();
    transformationDescription.setDataSource(new String[]{"dataflow.promotion_csv"});
    transformationDescription.setLimit(2000);
    transformationDescription.setJobType("transformation");
    transformationSqlGenerator = new TransformationSqlGenerator(transformationDescription);
  }

  @Test
  public void testExpression() {
    expression();
    String sql = transformationSqlGenerator.generateDataSourceSql();
    log.info(sql);
    clickHouseJdbcUtils.queryForList(sql);
  }
  private void expression(){
    Expression expression1 = new Expression();
    expression1.setExpression("replaceAll(Married,'married','已婚')");
    expression1.setNewColumnName("NewMarried1_1");
    Expression expression2 = new Expression();
    expression2.setExpression("replaceAll(Married,'single','单身')");
    expression2.setNewColumnName("NewMarried1_2");
    List<Expression> list = ImmutableList.of(expression1,expression2);
    transformationDescription.setExpressions(list);
  }

  @Test
  public void testFindReplace() {
    findReplace();
    String sql = transformationSqlGenerator.generateDataSourceSql();
    log.info(sql);
    clickHouseJdbcUtils.queryForList(sql);
  }
  private void findReplace(){
    FindReplace findReplace = new FindReplace();
    findReplace.setSearchInColumnName("Married");
    findReplace.setSearch("single");
    findReplace.setNewColumnName("NewMarried2");
    findReplace.setReplaceWith("单身");
    List<FindReplace> list = ImmutableList.of(findReplace);
    transformationDescription.setFindReplaces(list);
  }

  @Test
  public void testBinarizer() {
    binarizer();
    String sql = transformationSqlGenerator.generateDataSourceSql();
    log.info(sql);
    clickHouseJdbcUtils.queryForList(sql);
  }
  private void binarizer(){
    Binarizer binarizer = new Binarizer();
    binarizer.setNewColumnName("NewMarried3");
    binarizer.setFilter("Married = 'single'");
    List<Binarizer> list = ImmutableList.of(binarizer);
    transformationDescription.setBinarizers(list);
  }
  @Test
  public void testDataType() {
    // todo 语法错误
    DataType dataType = new DataType();
    dataType.setColumnName("DealerId");
    dataType.setDataType("Int32");
    List<DataType> list = ImmutableList.of(dataType);
    transformationDescription.setDataTypes(list);
    String sql = transformationSqlGenerator.generateDataSourceSql();
    log.info(sql);
    clickHouseJdbcUtils.queryForList(sql);
  }

  @Test
  public void testCustomBinning() {
    customBinning();
    String sql = transformationSqlGenerator.generateDataSourceSql();
    log.info(sql);
    clickHouseJdbcUtils.queryForList(sql);
  }

  private void customBinning() {
    CustomBinning customBinning = new CustomBinning();
    customBinning.setNewColumnName("NewMarried4");
    customBinning.setDefaultBin("-1");
    customBinning.setIsNumeric(false);
    customBinning.setBins(
        ImmutableList.of(new Bin("1", "Married = 'single'"), new Bin("2", "Married = 'married'")));
    transformationDescription.setCustomBinnings(ImmutableList.of(customBinning));

  }

  @Test
  public void testAll() {
    customBinning();
    findReplace();
    expression();
    binarizer();
    String sql = transformationSqlGenerator.generateDataSourceSql();
    log.info(sql);
    clickHouseJdbcUtils.queryForList(sql);
  }
}
