package ${conf.basePackage}.${conf.servicePackage}<#if prefixName??>.${prefixName}</#if>;

<#list tables as table>
import ${conf.basePackage}.${conf.beanPackage}<#if prefixName??>.${prefixName}</#if>.${table.beanName};
</#list>
import java.util.Map;
import java.util.List;
import org.springframework.data.domain.Page;

/**
 * @类说明：
 * ${comments}
 * @version 1.0
 * @创建时间：${.now}
 */
public interface ${beanName}Service {

<#list tables as table>
	<#assign primaryKey = table.primaryKey/>
	<#assign keys = primaryKey?keys/>
	<#list keys as key>
		<#assign pk_column = key/>
		<#assign pk_field = primaryKey[key]/>
    </#list>
	//****************************************** ${table.tableDesc}${table.beanName} **********************************************/
	void insert${table.beanName}(${table.beanName} ${table.beanName?uncap_first});
	void update${table.beanName}(${table.beanName} ${table.beanName?uncap_first});
	${table.beanName} load${table.beanName}By${pk_field?cap_first}(long ${pk_field?uncap_first});
	List<${table.beanName}> loadAll${table.beanName}();
	List<${table.beanName}> load${table.beanName}ByParams(Map<String, Object> params);
	Page<${table.beanName}> load${table.beanName}ByParams(Map<String, Object> params, int pageNo, int pageSize);
	void insert${table.beanName}List(List<${table.beanName}> ${table.beanName?uncap_first}s);
	
</#list>
}