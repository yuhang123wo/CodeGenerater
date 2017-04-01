<#-- enum template -->
package ${conf.basePackage}.enums;

/**
 * ${field.columnRemarks}
 * @version 1.0
 * @创建时间：${.now}
 */
public class ${field.beanName?cap_first} {
<#assign enumValues = field.enumValues/>

<#list enumValues as enumValue>
    ${enumValue}{public String getName(){return "";}}<#if (enumValue?is_last)>;<#else>,</#if>
</#list>
	public abstract String getName();

}
