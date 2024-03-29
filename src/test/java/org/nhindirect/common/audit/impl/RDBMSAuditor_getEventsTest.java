package org.nhindirect.common.audit.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.UUID;

import javax.management.openmbean.CompositeData;

import org.nhindirect.common.audit.AuditEvent;
import org.nhindirect.common.audit.DefaultAuditContext;

public class RDBMSAuditor_getEventsTest extends RDBMSAuditorBaseTest
{
	private static final String PRINCIPAL = "JUNITTEST";
	
	@Test 
	public void testGetEvents_AssertGotAllRecordsRequested()
	{
		
		AuditEvent event1 = new AuditEvent("Category" + UUID.randomUUID(), "type");
		AuditEvent event2 = new AuditEvent("Category" + UUID.randomUUID(), "type");
		
		DefaultAuditContext context1 = new DefaultAuditContext("name1", "value1");
		DefaultAuditContext context2 = new DefaultAuditContext("name2", "value2");
		
		auditorImpl.audit(PRINCIPAL, event1);
		auditorImpl.audit(PRINCIPAL, event2, Arrays.asList(context1, context2));
		
		CompositeData[] events = auditorImpl.getEvents(2);
		
		assertNotNull(events);
		assertEquals(2, events.length);
		
		CompositeData lastMessage = events[0];
		assertEquals(event2.getName(), lastMessage.get("Event Name"));
		assertEquals(event2.getType(), lastMessage.get("Event Type"));
		assertTrue(lastMessage.get("Event Id").toString().length() > 0);
		assertTrue(lastMessage.get("Event Time").toString().length() > 0);
		assertNotNull(lastMessage.get("Contexts"));
		String[] contexts = (String[])lastMessage.get("Contexts");
		assertEquals(2, contexts.length);
		
		assertEquals("name1:value1", contexts[0]);
		assertEquals("name2:value2", contexts[1]);
	}		
	
	@Test 
	public void testGetEvents_RequestMoreThanAvailable_AssertGotAllAvailableRecords()
	{

		
		AuditEvent event1 = new AuditEvent("Category" + UUID.randomUUID(), "type");
		AuditEvent event2 = new AuditEvent("Category" + UUID.randomUUID(), "type");
		
		DefaultAuditContext context1 = new DefaultAuditContext("name1", "value1");
		DefaultAuditContext context2 = new DefaultAuditContext("name2", "value2");
		
		auditorImpl.audit(PRINCIPAL, event1);
		auditorImpl.audit(PRINCIPAL, event2, Arrays.asList(context1, context2));
		
		CompositeData[] events = auditorImpl.getEvents(5);
		
		assertNotNull(events);
		assertEquals(2, events.length);
		
		CompositeData lastMessage = events[0];
		assertEquals(event2.getName(), lastMessage.get("Event Name"));
		assertEquals(event2.getType(), lastMessage.get("Event Type"));
		assertTrue(lastMessage.get("Event Id").toString().length() > 0);
		assertTrue(lastMessage.get("Event Time").toString().length() > 0);
		assertNotNull(lastMessage.get("Contexts"));
		String[] contexts = (String[])lastMessage.get("Contexts");
		assertEquals(2, contexts.length);
		
		assertEquals("name1:value1", contexts[0]);
		assertEquals("name2:value2", contexts[1]);
	}		
	
	@Test 
	public void testGetEvents_RequestLessThanAvailable_AssertGotOnlyRecords()
	{
		
		AuditEvent event1 = new AuditEvent("Category" + UUID.randomUUID(), "type");
		AuditEvent event2 = new AuditEvent("Category" + UUID.randomUUID(), "type");
		
		DefaultAuditContext context1 = new DefaultAuditContext("name1", "value1");
		DefaultAuditContext context2 = new DefaultAuditContext("name2", "value2");
		
		auditorImpl.audit(PRINCIPAL, event1);
		auditorImpl.audit(PRINCIPAL, event2, Arrays.asList(context1, context2));
		
		CompositeData[] events = auditorImpl.getEvents(1);
		
		assertNotNull(events);
		assertEquals(1, events.length);
		
		CompositeData lastMessage = events[0];
		assertEquals(event2.getName(), lastMessage.get("Event Name"));
		assertEquals(event2.getType(), lastMessage.get("Event Type"));
		assertTrue(lastMessage.get("Event Id").toString().length() > 0);
		assertTrue(lastMessage.get("Event Time").toString().length() > 0);
		assertNotNull(lastMessage.get("Contexts"));
		String[] contexts = (String[])lastMessage.get("Contexts");
		assertEquals(2, contexts.length);
		
		assertEquals("name1:value1", contexts[0]);
		assertEquals("name2:value2", contexts[1]);
	}		
	
	@Test 
	public void testGetEvents_NoRecordsAvailable_NoRecordsFound()
	{
		
		CompositeData[] events = auditorImpl.getEvents(1);
		
		assertNull(events);
	}	
	
	@Test 
	public void testGetEvents_NoRecordsRequested_ReturnedRecords()
	{		
		AuditEvent event1 = new AuditEvent("Category" + UUID.randomUUID(), "type");
		AuditEvent event2 = new AuditEvent("Category" + UUID.randomUUID(), "type");
		
		DefaultAuditContext context1 = new DefaultAuditContext("name1", "value1");
		DefaultAuditContext context2 = new DefaultAuditContext("name2", "value2");
		
		auditorImpl.audit(PRINCIPAL, event1);
		auditorImpl.audit(PRINCIPAL, event2, Arrays.asList(context1, context2));
		
		CompositeData[] events = auditorImpl.getEvents(0);
		
		assertNull(events);

	}		
}
