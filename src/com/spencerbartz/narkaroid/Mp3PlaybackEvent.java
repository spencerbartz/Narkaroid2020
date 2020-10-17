package com.spencerbartz.narkaroid;

public class Mp3PlaybackEvent {
	public PausableMp3Player source;
	public EventType eventType;
	public int frameIndex;

	public Mp3PlaybackEvent(PausableMp3Player source, EventType eventType, int frameIndex) {
		this.source     = source;
		this.eventType  = eventType;
		this.frameIndex = frameIndex;
	}

	/**
	 * EventType
	 */
	public static class EventType {
		public String name;

		public EventType(String name) {
			this.name = name;
		}
		
		/**
		 * class Instances
		 * This makes the 4 EventTypes singletons. When creating Mp3PlaybackEvents, one of
		 * these 4 can be referenced directly and passed to the Mp3PlaybackEvent constructor.
		 */
		public static class Instances {
			public static EventType Started  = new EventType("Started");
			public static EventType Stopped  = new EventType("Stopped");
			public static EventType Paused   = new EventType("Paused");
			public static EventType Finished = new EventType("Finished");
		}
	}
}
