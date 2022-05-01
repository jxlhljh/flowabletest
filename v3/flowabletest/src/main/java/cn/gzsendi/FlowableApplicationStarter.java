package cn.gzsendi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 通用的流程设计查看(不要删除)：http://localhost:8080/flowabletest/modeler/#/processes/c1ad50ed-c7e2-11ec-8b32-02004c4f4f50
 * @author jxlhl
 * 测试URL：先从普通人员页面开始测试即可,即：http://localhost:8080/flowabletest/main.html?userId=lix
 * 普通人员：http://localhost:8080/flowtest/main.html?userId=lix
 * 项目经理：http://localhost:8080/flowtest/main.html?userId=zhaoyl
 * 部门经理：http://localhost:8080/flowtest/main.html?userId=linjh
 * 总经理：http://localhost:8080/flowtest/main.html?userId=xuw
 */
@SpringBootApplication
public class FlowableApplicationStarter {

	public static void main(String[] args) {
		
		//SpringApplicationBuilder builder = new SpringApplicationBuilder(DemoApplicationStarter.class);
		//builder.headless(false).run(args);
		SpringApplication.run(FlowableApplicationStarter.class, args);
		
	}

}
