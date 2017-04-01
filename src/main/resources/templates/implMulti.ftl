<#import "base/date.ftl" as dt>
package ${conf.basePackage}.${conf.servicePackage}<#if prefixName??>.${prefixName}</#if>.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import ${conf.basePackage}.${conf.beanPackage}<#if prefixName??>.${prefixName}</#if>.${beanName};
import ${conf.basePackage}.${conf.servicePackage}<#if prefixName??>.${prefixName}</#if>.${beanName}Service;
import ${conf.basePackage}.${conf.mapperPackage}<#if prefixName??>.${prefixName}</#if>.${beanName}Dao;

/**
 * @类说明：
 * ${comments}
 * @version 1.0
 * @创建时间：${.now}
 */
@Component("${beanName?uncap_first}Service")
public class ${beanName}ServiceImpl implements ${beanName}Service {

	@Autowired
	private ${beanName}Dao ${beanName?uncap_first}Dao;

<#list tables as table>
	<#assign primaryKey = table.primaryKey/>
	<#assign keys = primaryKey?keys/>
	<#list keys as key>
		<#assign pk_column = key/>
		<#assign pk_field = primaryKey[key]/>
    </#list>
	
	//****************************************** ${table.tableDesc}${table.beanName} **********************************************/
	@Override
	public void insert${table.beanName}(${table.beanName} ${table.beanName?uncap_first}) {
		${beanName?uncap_first}Dao.insert${table.beanName}(${table.beanName?uncap_first});
	}

	@Override
	public void update${table.beanName}(${table.beanName} ${table.beanName?uncap_first}) {
		${beanName?uncap_first}Dao.update${table.beanName}(${table.beanName?uncap_first});
	}

	@Override
	public ${table.beanName} load${table.beanName}By${pk_field?cap_first}(long ${pk_field?uncap_first}) {
		return ${beanName?uncap_first}Dao.load${table.beanName}By${pk_field?cap_first}(${pk_field?uncap_first});
	}

	@Override
	public List<${table.beanName}> loadAll${table.beanName}() {
		return ${beanName?uncap_first}Dao.loadAll${table.beanName}();
	}

	@Override
	public List<${table.beanName}> load${table.beanName}ByParams(Map<String, Object> params) {
		return ${beanName?uncap_first}Dao.load${table.beanName}ByParams(params);
	}

	@Override
	public Page<${table.beanName}> load${table.beanName}ByParams(Map<String, Object> params, int pageNo, int pageSize) {
		PageRequest pageRequest = new PageRequest(pageNo - 1, pageSize);
		if (params == null) {
			params = new HashMap<String, Object>();
		}
		params.put("start", pageRequest.getOffset());
		params.put("size", pageSize);
		int count = ${beanName?uncap_first}Dao.count${table.beanName}ByParams(params);
		List<${table.beanName}> list = ${beanName?uncap_first}Dao.load${table.beanName}ByParams(params);
		return new PageImpl<${table.beanName}>(list, pageRequest, count);
	}
	
	@Override
	public void insert${table.beanName}List(List<${table.beanName}> ${table.beanName?uncap_first}s) {
		${beanName?uncap_first}Dao.insert${table.beanName}List(${table.beanName?uncap_first}s);
	}

</#list>
}