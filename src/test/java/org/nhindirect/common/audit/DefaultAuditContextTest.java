package org.nhindirect.common.audit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;

public class DefaultAuditContextTest 
{
	@Test
	public void testConstructAuditContext() throws Exception
	{
		DefaultAuditContext context = new DefaultAuditContext("test", "");
		
		assertNotNull(context);
	}
	
	@Test
	public void testGetName() throws Exception
	{
		DefaultAuditContext context = new DefaultAuditContext("name", "value");
		
		assertEquals("name", context.getContextName());
	}	
	
	@Test
	public void testGetValue() throws Exception
	{
		DefaultAuditContext context = new DefaultAuditContext("name", "value");
		
		assertEquals("value", context.getContextValue());
	}		
	
	@Test
	public void testGetEmptyValue() throws Exception
	{
		DefaultAuditContext context = new DefaultAuditContext("name", "");
		
		assertEquals("", context.getContextValue());
	}		
	
	@Test
	public void testConstructAuditContext_EmptyName_AssertIllgalArgumentException() throws Exception
	{
		Assertions.assertThrows(IllegalArgumentException.class, () ->
		{
			new DefaultAuditContext("", "value");
		});
	}	
	
	@Test
	public void testConstructAuditContext_NullName_AssertIllgalArgumentException() throws Exception
	{
		Assertions.assertThrows(IllegalArgumentException.class, () ->
		{
			new DefaultAuditContext(null, "value");
		});
	}	
	
	@Test
	public void testConstructAuditContext_NullValue_AssertIllgalArgumentException() throws Exception
	{
		Assertions.assertThrows(IllegalArgumentException.class, () ->
		{
			new DefaultAuditContext("name", null);
		});
	}		
	
}
