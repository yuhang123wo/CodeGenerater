<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<#assign beanName = table.beanName/>
<#assign tableName = table.tableName/>
<#macro mapperEl value>${r"#{"}${value}}</#macro>
<#assign primaryKey = table.primaryKey/>
<#assign keys = primaryKey?keys/>
<#assign mapper = conf.basePackage+"."+conf.mapperPackage+"."+beanName+"Dao"/>
<!-- namespace必须指向Dao接口 -->
<mapper namespace="${mapper}">
	<#list keys as key>
		<#assign pk_column = key/>
		<#assign pk_field = primaryKey[key]/>
    </#list>
	<!-- ****************************************** ${table.tableDesc}${table.beanName} ********************************************** -->
	<sql id="select${beanName}">
    	select <#list table.fieldInfos as fieldInfo>${fieldInfo.columnName}<#if fieldInfo.columnName?contains("_")> ${fieldInfo.beanName}</#if><#if !(fieldInfo?is_last)>,</#if></#list>
	</sql>
	
	<sql id="where${beanName}">
        <where>
        	<!-- <if test="xxx!=null">
        		and xxx_xx=<@mapperEl "xxx"/>
        	</if>
        	<if test="xxx!=null">
        		and xxx_xx like <@mapperEl "xxx"/>
        	</if>
        	<if test="ids!=null">
				and xxx_id in(
				<foreach collection="ids" item="id" separator=",">
						<@mapperEl "id"/> 
				</foreach>
				)
			</if>
        	<if test="startTime!=null">
	        	<![CDATA[
	        		and create_time>=<@mapperEl "startTime"/>
	        	]]>
	        </if>
	        <if test="endTime!=null">
	        	<![CDATA[
	        		and create_time<=<@mapperEl "endTime"/>
	        	]]>
	        </if> -->
        </where>
    </sql>
    
    <select id="load${beanName}ByParams" parameterType="map" resultType="${beanName}">
        <include refid="select${beanName}"/> FROM ${tableName} <include refid="where${beanName}"/>
        order by ${pk_column} desc 
        <if test="start!=null and size!=null">
	        limit <@mapperEl "start"/>,<@mapperEl "size"/>
        </if>
    </select>
    
    <select id="count${beanName}ByParams" parameterType="map" resultType="int">
        select count(1) from ${tableName} <include refid="where${beanName}"/>
    </select>
    
    <insert id="insert${beanName}List" parameterType="java.util.List">
		insert into ${tableName}(<#list table.fieldInfos as fieldInfo><#if fieldInfo.columnName!="id">${fieldInfo.columnName}<#if !(fieldInfo?is_last)>,</#if></#if></#list>) values
		<foreach collection="list" item="item" separator=",">
			(<#list table.fieldInfos as fieldInfo><#if fieldInfo.beanName!="id"><@mapperEl "item."+fieldInfo.beanName/><#if !(fieldInfo?is_last)>,</#if></#if></#list>)
		</foreach>
	</insert>

</mapper>