package com.bdilab.dataflow.sql.generator;

import com.bdilab.dataflow.dto.Bin;
import com.bdilab.dataflow.dto.Binarizer;
import com.bdilab.dataflow.dto.CustomBinning;
import com.bdilab.dataflow.dto.DataType;
import com.bdilab.dataflow.dto.Expression;
import com.bdilab.dataflow.dto.FindReplace;
import com.bdilab.dataflow.dto.jobdescription.TransformationDesc;
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
  TransformationDesc transformationDesc;

  @Resource
  ClickHouseJdbcUtils clickHouseJdbcUtils;

  @Value("${clickhouse.http.url}")
  private String httpPrefix;

  /**
   * initialize .
   */
  @Before
  public void init() {
    transformationDesc = new TransformationDesc();
    transformationDesc.setDataSource(new String[]{"dataflow.promotion_csv"});
    transformationDesc.setLimit(2000);
    transformationDesc.setJobType("transformation");
    transformationSqlGenerator = new TransformationSqlGenerator(transformationDesc);
  }

  @Test
  public void testExpression() {
    Expression expression = new Expression();
    expression.setExpression("replaceAll(Married,'married','已婚')");
    expression.setNewColumnName("NewMarried");
    List<Expression> list = ImmutableList.of(expression);
    transformationDesc.setExpressions(list);
    String sql = transformationSqlGenerator.generateDataSourceSql();
    log.info(sql);
    clickHouseJdbcUtils.queryForList(sql);
  }

  @Test
  public void testFindReplace() {
    FindReplace findReplace = new FindReplace();
    findReplace.setSearchInColumnName("Married");
    findReplace.setSearch("single");
    findReplace.setNewColumnName("NewMarried");
    findReplace.setReplaceWith("单身");
    List<FindReplace> list = ImmutableList.of(findReplace);
    transformationDesc.setFindReplaces(list);
    String sql = transformationSqlGenerator.generateDataSourceSql();
    log.info(sql);
    clickHouseJdbcUtils.queryForList(sql);
  }

  @Test
  public void testBinarizer() {
    Binarizer binarizer = new Binarizer();
    binarizer.setNewColumnName("NewMarried");
    binarizer.setFilter("Married = 'single'");
    List<Binarizer> list = ImmutableList.of(binarizer);
    transformationDesc.setBinarizers(list);
    String sql = transformationSqlGenerator.generateDataSourceSql();
    log.info(sql);
    clickHouseJdbcUtils.queryForList(sql);
  }

  @Test
  public void testDataType() {
    // todo 语法错误
    DataType dataType = new DataType();
    dataType.setColumnName("DealerId");
    dataType.setDataType("Int32");
    List<DataType> list = ImmutableList.of(dataType);
    transformationDesc.setDataTypes(list);
    String sql = transformationSqlGenerator.generateDataSourceSql();
    log.info(sql);
    clickHouseJdbcUtils.queryForList(sql);
  }

  @Test
  public void testCustomBinning() {
    CustomBinning customBinning = new CustomBinning();
    customBinning.setNewColumnName("NewMarried");
    customBinning.setDefaultBin("-1");
    customBinning.setIsNumeric(false);
    customBinning.setBins(
        ImmutableList.of(new Bin("1", "Married = 'single'"), new Bin("2", "Married = 'married'")));
    transformationDesc.setCustomBinnings(ImmutableList.of(customBinning));
    String sql = transformationSqlGenerator.generateDataSourceSql();
    log.info(sql);
    clickHouseJdbcUtils.queryForList(sql);
  }

  @Test
  public void testCustomBinningWithNumeric() {

  }
}
