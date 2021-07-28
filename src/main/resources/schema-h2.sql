CREATE TABLE IF NOT EXISTS auditevent ( id bigint(20) NOT NULL AUTO_INCREMENT, uuid VARCHAR(40) NOT NULL, principal VARCHAR(255) NOT NULL, eventName VARCHAR(255) NOT NULL,
eventType VARCHAR(255) NOT NULL, eventTime datetime DEFAULT NULL, PRIMARY KEY (id));
			
CREATE TABLE IF NOT EXISTS auditcontext ( id bigint(20) NOT NULL AUTO_INCREMENT, contextName VARCHAR(40) NOT NULL, contextValue VARCHAR(255) NOT NULL, 
  auditEventId bigint(20) NOT NULL, PRIMARY KEY (id), CONSTRAINT FK_3au2yxghx7asdfvchv1sakel FOREIGN KEY (auditEventId) REFERENCES auditevent (id));
CREATE INDEX IF NOT EXISTS directAuditEventIdIdx ON auditcontext(auditEventId);
