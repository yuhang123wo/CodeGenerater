<#-- bean template -->
package ${conf.basePackage}.${conf.beanPackage}<#if prefixName??>.${prefixName}</#if>;
<#assign primaryKey = table.primaryKey/>
<#assign keys = primaryKey?keys/>
<#list keys as key>
	<#assign pk_column = key/>
	<#assign pk_field = primaryKey[key]/>
</#list>

<#if pk_field=="id">${conf.entityPackage}</#if>
<#list table.packages as package>
${package}
</#list>
/**
 * @类说明： 
 * ${table.tableDesc}
 * @version 1.0
 * @创建时间：${.now}
 */
public class ${table.beanName}<#if pk_field=="id"> extends Entity</#if> {
<#assign fieldInfos = table.fieldInfos/>

<#if pk_field=="id">	private static final long serialVersionUID = 1L;</#if>
<#list fieldInfos as fieldInfo>
    <#if fieldInfo.beanName!="id">private ${fieldInfo.beanType} ${fieldInfo.beanName};<#if (fieldInfo.columnRemarks)!=""> // ${fieldInfo.columnRemarks}</#if></#if>
</#list>

<#list fieldInfos as fieldInfo>
<#if fieldInfo.beanName!="id">
    public ${fieldInfo.beanType} get${fieldInfo.beanName?cap_first}() {
        return this.${fieldInfo.beanName};
    }

    public void set${fieldInfo.beanName?cap_first}(${fieldInfo.beanType} ${fieldInfo.beanName}) {
        this.${fieldInfo.beanName} = ${fieldInfo.beanName};
    }
    
</#if>
</#list>
}
