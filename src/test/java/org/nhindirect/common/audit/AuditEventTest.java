package org.nhindirect.common.audit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;

public class AuditEventTest 
{
	@Test
	public void testConstructAuditEvent() throws Exception
	{
		AuditEvent event = new AuditEvent("category", "type");
		
		assertNotNull(event);
	}
	
	@Test
	public void testGetName() throws Exception
	{
		AuditEvent event = new AuditEvent("category", "type");
		
		assertEquals("category", event.getName());
	}	
	
	@Test
	public void testGetType() throws Exception
	{
		AuditEvent event = new AuditEvent("category", "type");
		
		assertEquals("type", event.getType());
	}		
		
	
	@Test
	public void testConstructAuditEvent_EmptyName_AssertIllgalArgumentException() throws Exception
	{
		Assertions.assertThrows(IllegalArgumentException.class, () ->
		{
			new AuditEvent("", "value");
		});
	}	
	
	@Test
	public void testConstructAuditEvent_NullName_AssertIllgalArgumentException() throws Exception
	{
		Assertions.assertThrows(IllegalArgumentException.class, () ->
		{
			new AuditEvent(null, "value");
		});
	}	
	
	@Test
	public void testConstructAuditEvent_NullType_AssertIllgalArgumentException() throws Exception
	{
		Assertions.assertThrows(IllegalArgumentException.class, () ->
		{
			new AuditEvent("category", null);
		});
	
	}	
	
	@Test
	public void testConstructAuditEvent_EmptyType_AssertIllgalArgumentException() throws Exception
	{
		Assertions.assertThrows(IllegalArgumentException.class, () ->
		{
			new AuditEvent("category", "");
		});
	}	

	@Test
	public void testEquals() throws Exception
	{
		AuditEvent event1 = new AuditEvent("category", "type");
		AuditEvent event2 = new AuditEvent("category", "type");

		
		assertTrue(event1.equals(event2));
	}	
	
	@Test
	public void testEquals_DifferentName_AssertNotEqual() throws Exception
	{
		AuditEvent event1 = new AuditEvent("category1", "type");
		AuditEvent event2 = new AuditEvent("category2", "type");

		
		assertFalse(event1.equals(event2));
	}		
	
	@Test
	public void testEquals_DifferentType_AssertNotEqual() throws Exception
	{
		AuditEvent event1 = new AuditEvent("category", "type1");
		AuditEvent event2 = new AuditEvent("category", "type2");

		
		assertFalse(event1.equals(event2));
	}		
	
	@Test
	public void testEquals_DifferentObject_AssertNotEqual() throws Exception
	{
		AuditEvent event = new AuditEvent("category", "type1");

		
		assertFalse(event.equals(event.toString()));
	}	
}