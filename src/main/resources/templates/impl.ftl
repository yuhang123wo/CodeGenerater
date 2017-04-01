<#import "base/date.ftl" as dt>
package ${conf.basePackage}.${conf.servicePackage}<#if prefixName??>.${prefixName}</#if>.impl;
<#assign beanName = table.beanName/>
<#assign beanNameUncap_first = beanName?uncap_first/>

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
 * ${table.tableDesc}
 * @创建时间：${.now}
 */
@Component("${beanNameUncap_first}Service")
public class ${beanName}ServiceImpl implements ${beanName}Service {
	<#assign primaryKey = table.primaryKey/>
	<#assign keys = primaryKey?keys/>
	<#list keys as key>
		<#assign pk_column = key/>
		<#assign pk_field = primaryKey[key]/>
    </#list>
        
	@Autowired
	private ${beanName}Dao ${beanNameUncap_first}Dao;
	
	//****************************************** ${table.tableDesc}${table.beanName} **********************************************/
	@Override
	public void insert${beanName}(${beanName} ${beanNameUncap_first}) {
		${beanNameUncap_first}Dao.insert${beanName}(${beanNameUncap_first});
	}

	@Override
	public void update${beanName}(${beanName} ${beanNameUncap_first}) {
		${beanNameUncap_first}Dao.update${beanName}(${beanNameUncap_first});
	}

	@Override
	public ${beanName} load${beanName}By${pk_field?cap_first}(long ${pk_field?uncap_first}) {
		return ${beanNameUncap_first}Dao.load${beanName}By${pk_field?cap_first}(${pk_field?uncap_first});
	}

	@Override
	public List<${table.beanName}> loadAll${table.beanName}() {
		return ${beanNameUncap_first}Dao.loadAll${beanName}();
	}

	@Override
	public List<${table.beanName}> load${table.beanName}ByParams(Map<String, Object> params) {
		return ${beanNameUncap_first}Dao.load${table.beanName}ByParams(params);
	}

	@Override
	public Page<${beanName}> load${beanName}ByParams(Map<String, Object> params, int pageNo, int pageSize) {
		PageRequest pageRequest = new PageRequest(pageNo - 1, pageSize);
		if (params == null) {
			params = new HashMap<String, Object>();
		}
		params.put("start", pageRequest.getOffset());
		params.put("size", pageSize);
		int count = ${beanNameUncap_first}Dao.count${beanName}ByParams(params);
		List<${beanName}> list = ${beanNameUncap_first}Dao.load${beanName}ByParams(params);
		return new PageImpl<${beanName}>(list, pageRequest, count);
	}

	@Override
	public void insert${beanName}List(List<${beanName}> ${beanName?uncap_first}s) {
		${beanNameUncap_first}Dao.insert${beanName}List(${beanName?uncap_first}s);
	}
}