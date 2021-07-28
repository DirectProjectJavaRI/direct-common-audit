package org.nhindirect.common.audit.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;


import org.junit.jupiter.api.extension.ExtendWith;
import org.nhindirect.common.audit.R2DBCTestConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = R2DBCTestConfiguration.class)
@TestPropertySource("classpath:bootstrap.properties")
public abstract class RDBMSAuditorBaseTest 
{
	@Autowired
	protected RDBMSAuditEventRepository eventRepo;		
	
	@Autowired
	protected RDBMSAuditContextRepository contextRepo;		
	
	protected RDBMSAuditor auditorImpl;
	
	@BeforeEach
	public void setUp()
	{
		clearAuditEvent();
		
		auditorImpl = new RDBMSAuditor();
		auditorImpl.setEventRepo(eventRepo);
		auditorImpl.setContextRepo(contextRepo);
	}
	
	protected void clearAuditEvent()
	{
		contextRepo.deleteAll().block();
		eventRepo.deleteAll().block();
		
		assertEquals(0L, eventRepo.count().block());
		assertEquals(0L, contextRepo.count().block());
	}
	
}
