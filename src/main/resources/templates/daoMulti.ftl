package ${conf.basePackage}.${conf.mapperPackage}<#if prefixName??>.${prefixName}</#if>;

<#list tables as table>
import ${conf.basePackage}.${conf.beanPackage}<#if prefixName??>.${prefixName}</#if>.${table.beanName};
</#list>
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Param;

/**
 * @类说明：
 * ${comments}
 * @version 1.0
 * @创建时间：${.now}
 */
@MyBatisRepository
public interface ${beanName}Dao {

<#list tables as table>
	<#macro mapperEl value>${r"#{"}${value}}</#macro>
	<#assign primaryKey = table.primaryKey/>
	<#assign keys = primaryKey?keys/>
	<#list keys as key>
		<#assign pk_column = key/>
		<#assign pk_field = primaryKey[key]/>
    </#list>

	//****************************************** ${table.tableDesc}${table.beanName} **********************************************/
	String ${table.beanName?uncap_first}_fields = "<#list table.fieldInfos as fieldInfo><#if fieldInfo.columnName!="id">${fieldInfo.columnName}<#if !(fieldInfo?is_last)>,</#if></#if></#list>";
	String ${table.beanName?uncap_first}_results = "<#list table.fieldInfos as fieldInfo><#if fieldInfo.beanName!="id"><@mapperEl fieldInfo.beanName/><#if !(fieldInfo?is_last)>,</#if></#if></#list>";
	String ${table.beanName?uncap_first}_fieldsAlais = "<#list table.fieldInfos as fieldInfo>${fieldInfo.columnName}<#if fieldInfo.columnName?contains("_")> ${fieldInfo.beanName}</#if><#if !(fieldInfo?is_last)>,</#if></#list>";
	String ${table.beanName?uncap_first}_updateStr = "<#list table.fieldInfos as fieldInfo><#if fieldInfo.columnName!="id">${fieldInfo.columnName}=<@mapperEl fieldInfo.beanName/><#if !(fieldInfo?is_last)>,</#if></#if></#list>";
	
	@Insert("insert into ${table.tableName}(" + ${table.beanName?uncap_first}_fields + ") values(" + ${table.beanName?uncap_first}_results + ")")
	@SelectKey(statement = "SELECT LAST_INSERT_ID() AS id", keyProperty = "id", before = false, resultType = long.class)
    void insert${table.beanName}(${table.beanName} ${table.beanName?uncap_first});

	@Update("update ${table.tableName} set " + ${table.beanName?uncap_first}_updateStr + " where ${pk_column}=<@mapperEl pk_field/>")
    void update${table.beanName}(${table.beanName} ${table.beanName?uncap_first});
    
    @Select("select " + ${table.beanName?uncap_first}_fieldsAlais + " from ${table.tableName} where ${pk_column}=<@mapperEl pk_field/>")
	${table.beanName} load${table.beanName}By${pk_field?cap_first}(long ${pk_field?uncap_first});
	
	@Select("select " + ${table.beanName?uncap_first}_fieldsAlais + " from ${table.tableName}")
	List<${table.beanName}> loadAll${table.beanName}();
	
	List<${table.beanName}> load${table.beanName}ByParams(Map<String, Object> params);
	int count${table.beanName}ByParams(Map<String, Object> params);
	
	void insert${table.beanName}List(@Param("list")List<${table.beanName}> ${table.beanName?uncap_first}s);
</#list>

}