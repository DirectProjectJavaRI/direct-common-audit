package org.nhindirect.common.audit.impl;

import static org.junit.Assert.assertEquals;


import org.junit.Before;
import org.junit.runner.RunWith;
import org.nhindirect.common.audit.JPATestConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional
@ContextConfiguration(classes=JPATestConfiguration.class)
public abstract class RDBMSAuditorBaseTest 
{
	@Autowired
	protected RDBMSDao auditor;		
	
	protected RDBMSAuditor auditorImpl;
	
	@Before
	public void setUp()
	{
		clearAuditEvent();
		
		auditorImpl = new RDBMSAuditor();
			auditorImpl.setDao(auditor);
	}
	
	protected void clearAuditEvent()
	{
		auditor.rDBMSclear();
		
		assertEquals((Integer)0, auditor.getRDBMSEventCount());
	}
	
}
