package ${conf.basePackage}.${conf.servicePackage}<#if prefixName??>.${prefixName}</#if>;

<#assign beanName = table.beanName/>
import ${conf.basePackage}.${conf.beanPackage}<#if prefixName??>.${prefixName}</#if>.${beanName};
import java.util.Map;
import java.util.List;
import org.springframework.data.domain.Page;

/**
 * @类说明：
 * ${table.tableDesc}
 * @创建时间：${.now}
 */
public interface ${beanName}Service {

	<#assign primaryKey = table.primaryKey/>
	<#assign keys = primaryKey?keys/>
	<#list keys as key>
		<#assign pk_column = key/>
		<#assign pk_field = primaryKey[key]/>
    </#list>
	//****************************************** ${table.tableDesc}${table.beanName} **********************************************/
	void insert${beanName}(${beanName} ${beanName?uncap_first});
	void update${beanName}(${beanName} ${beanName?uncap_first});
	${beanName} load${beanName}By${pk_field?cap_first}(long ${pk_field?uncap_first});
	List<${table.beanName}> loadAll${table.beanName}();
	List<${table.beanName}> load${table.beanName}ByParams(Map<String, Object> params);
	Page<${beanName}> load${beanName}ByParams(Map<String, Object> params, int pageNo, int pageSize);
	void insert${beanName}List(List<${beanName}> ${beanName?uncap_first}s);
	
}