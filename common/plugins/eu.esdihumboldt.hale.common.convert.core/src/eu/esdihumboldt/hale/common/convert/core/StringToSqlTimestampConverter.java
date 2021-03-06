/*
 * Copyright (c) 2012 Data Harmonisation Panel
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution. If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     HUMBOLDT EU Integrated Project #030962
 *     Data Harmonisation Panel <http://www.dhpanel.eu>
 */

package eu.esdihumboldt.hale.common.convert.core;

import java.sql.Timestamp;

import org.springframework.core.convert.converter.Converter;

/**
 * Convert a {@link String} to a {@link Timestamp}.
 * 
 * @author Simon Templer
 */
public class StringToSqlTimestampConverter implements Converter<String, Timestamp> {

	private static final StringToJodaDateTimeConverter stringToJoda = new StringToJodaDateTimeConverter();

	private static final JodaDateTimeToSqlTimestampConverter jodaToTimestamp = new JodaDateTimeToSqlTimestampConverter();

	@Override
	public Timestamp convert(String source) {
		if (source == null) {
			return null;
		}
		try {
			// try with default format
			return Timestamp.valueOf(source);
		} catch (IllegalArgumentException e) {
			// fall back to joda parser
			return jodaToTimestamp.convert(stringToJoda.convert(source));
		}
	}
}
