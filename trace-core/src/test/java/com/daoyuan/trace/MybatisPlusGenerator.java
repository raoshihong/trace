package com.daoyuan.trace.client;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MybatisPlusGenerator {


	/**
	 * RUN THIS
	 */
	public static void main(String[] args) {
//		generator("event");
//		generator("event_content");
//		generator("event_place");
//		generator("event_user");
		generator("table_config");
		generator("trace_config");
		generator("table_property_config");
	}

	public static void generator(String tableName){

		// 代码生成器
		AutoGenerator mpg = new AutoGenerator();

		// 全局配置

		/* ------------------------可改动的地方------------------------ */
		// 项目输出目录
		String projectParentPath = System.getProperty("user.dir");
		// 项目名称
		String projectName = "trace";
		String jdbcDriver = "com.mysql.jdbc.Driver";
		String jdbcUrl = "jdbc:mysql://localhost:3306/trace?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8";
		String jdbcUserName = "root";
		String jdbcPsw = "123456";

		String baseCodePackage = "com.daoyuan.trace";
		String entityPackage = "entity";
		// 下划线转驼峰
		boolean underlineToCamel = true;
		/* ------------------------可改动的地方------------------------ */

		// 项目全路径
		String projectPath = projectParentPath + File.separator + projectName;
		String javaOutputDir = projectPath + "/src/main/java";
		String mapperOutputDir = projectPath + "/src/main/resources/mapper/";

		//全局配置
		GlobalConfig gc = new GlobalConfig();
		gc.setOutputDir(javaOutputDir);
		gc.setAuthor("");
		gc.setOpen(false);
		mpg.setGlobalConfig(gc);

		// 数据源配置
		DataSourceConfig dsc = new DataSourceConfig();
		dsc.setUrl(jdbcUrl);
		dsc.setDriverName(jdbcDriver);
		dsc.setUsername(jdbcUserName);
		dsc.setPassword(jdbcPsw);
		mpg.setDataSource(dsc);

		// 包配置
		PackageConfig pc = new PackageConfig();
		pc.setParent(baseCodePackage);
		pc.setEntity(entityPackage);

		mpg.setPackageInfo(pc);

		// 自定义配置
		InjectionConfig cfg = new InjectionConfig() {
			@Override
			public void initMap() {
				// to do nothing
			}
		};
		List<FileOutConfig> focList = new ArrayList<>();
		focList.add(new FileOutConfig("/templates/mapper.xml.ftl") {
			@Override
			public String outputFile(TableInfo tableInfo) {
				// todo: 过滤不要生成的字段
				// 自定义输入文件名称
				return mapperOutputDir + tableInfo.getEntityName() + "Mapper"
						+ StringPool.DOT_XML;
			}
		});
		cfg.setFileOutConfigList(focList);
		mpg.setCfg(cfg);
		mpg.setTemplate(new TemplateConfig().setXml(null));

		// 策略配置
		StrategyConfig strategy = new StrategyConfig();
		//表命名规则,是否采用驼峰
		strategy.setNaming(underlineToCamel ? NamingStrategy.underline_to_camel : NamingStrategy.nochange);
		//列命名规则
		strategy.setColumnNaming(underlineToCamel ? NamingStrategy.underline_to_camel : NamingStrategy.nochange);
//		 strategy.setSuperEntityClass("com.baomidou.mybatisplus.samples.generator.common.BaseEntity");
		//设置实体是否使用Lombok管理
		strategy.setEntityLombokModel(true);
//		 strategy.setSuperControllerClass("com.baomidou.mybatisplus.samples.generator.common.BaseController");
		//设置需要包含的表名
		strategy.setInclude(tableName);
		// strategy.setSuperEntityColumns("id");
//		strategy.setControllerMappingHyphenStyle(true);
		strategy.setRestControllerStyle(true);
		//设置表的前缀
		strategy.setTablePrefix("t_");
		mpg.setStrategy(strategy);
		// 选择 freemarker 引擎需要指定如下加，注意 pom 依赖必须有！
		mpg.setTemplateEngine(new FreemarkerTemplateEngine());
		mpg.execute();
	}
}
