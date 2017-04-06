package org.iii.nmi.air.dao;

import java.sql.PreparedStatement;

public interface StatementParamsSetter
{
	public void setParametersOf(PreparedStatement st) throws SqlHandlerException;
}
