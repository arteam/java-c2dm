package com.notnoop.c2dm;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

/**
 * Represents an GCM notification to be sent to Google service.
 */
public interface GCMNotification {

    public List<String> getRegistrationIds();


	/**
	 * Returns the collapse key of the current message. The key is used by the
	 * GCM to group messages when the device is offline. Only the last message
	 * of each group will be sent to the device when it comes online. However,
	 * only a maximum of 4 notifications will be retained.
	 * 
	 * @return the notification collapse key
	 */
	public String getCollapseKey();

	/**
	 * Indicates whether the notification shall be delayed while the device is
	 * idle. As soon as the device becomes active the notification will be sent.
	 * 
	 * @return true when notification is delayed, false otherwise
	 */
	public boolean isDelayWhileIdle();

	/**
	 * Returns the payload data.
	 * 
	 * Payload data, expressed as key-value pairs. If present, it will be
	 * included in the Intent as application data, with the <key>. There is no
	 * limit on the number of key/value pairs, though there is a limit on the
	 * total size of the message.
	 * 
	 * @return the payload key-value pairs
	 */
	public JsonObject getData();
}