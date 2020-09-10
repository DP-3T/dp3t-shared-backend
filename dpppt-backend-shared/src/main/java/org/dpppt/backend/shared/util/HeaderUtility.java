package org.dpppt.backend.shared.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import org.springframework.http.HttpHeaders;

/**
 * Helper class for request header.
 * 
 * @author alig
 *
 */
public class HeaderUtility {

	private static ThreadLocal<SimpleDateFormat> headerDateFormatter = new ThreadLocal<SimpleDateFormat>();

	public static final String CACHE_CONTROL = "Cache-Control";
	public static final String NEXT_REFRESH = "X-Next-Refresh";
	public static final String MAX_AGE = "max-age";

	public static String createMaxAgeHeaderValue(int maxAgeSeconds) {
		return MAX_AGE + "=" + String.valueOf(maxAgeSeconds);
	}

	/**
	 * Creates headers with the following two entries:
	 * <ul>
	 * <li>Cache-Control: max-age=maxAgeSeconds</li>
	 * <li>X-Next-Refresh: now + nextRefreshSeconds
	 * </ul>
	 * 
	 * @param maxAgeSeconds
	 * @param nextRefreshSeconds
	 * @return
	 */
	public static HttpHeaders createHeaders(int maxAgeSeconds,
			int nextRefreshSeconds) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(CACHE_CONTROL, createMaxAgeHeaderValue(maxAgeSeconds));
		headers.add(NEXT_REFRESH,
				formatHeaderDate(getDate(nextRefreshSeconds * 1000L)));
		return headers;
	}

	/**
	 * Formats the date to a http timestamp.
	 * 
	 * @param date
	 * @return
	 */
	private static String formatHeaderDate(Date date) {
		SimpleDateFormat sdf = headerDateFormatter.get();
		if (sdf == null) {
			sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
			headerDateFormatter.set(sdf);
		}
		return sdf.format(date);
	}

	/**
	 * Returns the date from now plus the given number of milliseconds.
	 * 
	 * @param millis
	 * @return
	 */
	private static Date getDate(long millis) {
		return new Date(System.currentTimeMillis() + millis);
	}
}
