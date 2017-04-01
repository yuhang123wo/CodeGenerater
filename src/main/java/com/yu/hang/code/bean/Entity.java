package com.yu.hang.code.bean;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 
 * @author Administrator
 *
 */
public abstract class Entity implements Serializable {

	private static final long serialVersionUID = -1935978225755961366L;

	@Override
	public boolean equals(Object o) {
		return o != null && o.getClass().equals(this.getClass()) && EqualsBuilder.reflectionEquals(this, o);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
