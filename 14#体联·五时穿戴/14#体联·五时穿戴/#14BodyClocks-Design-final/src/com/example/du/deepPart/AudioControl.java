package com.example.du.deepPart;

import java.io.IOException;
import java.util.PriorityQueue;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

public class AudioControl {
	private static AudioControl instance = null;

	public static AudioControl getInstance() {
		if (instance == null)
			instance = new AudioControl();
		return instance;
	}

	private Context context;
	private MediaPlayer mediaPlayer = null;

	private boolean isRelease;

	public static enum Priority {
		WARNING, TIP
	}

	private class MyStruct implements Comparable<MyStruct> {

		public String filepath;
		public Priority priority;

		public MyStruct(String filepath, Priority priority) {
			this.filepath = filepath;
			this.priority = priority;
		}

		@Override
		public int compareTo(MyStruct other) {
			int result;
			if (priority == Priority.WARNING) {
				result = -1;
			} else if (other.priority == Priority.WARNING) {
				result = 1;
			} else {
				result = -1;
			}
			return result;
		}

		@Override
		public String toString() {
			return "\"filepath=" + filepath + "\"";
		}

	};

	private PriorityQueue<MyStruct> priorityQueue = null;

	private AudioControl() {
		mediaPlayer = new MediaPlayer();
		priorityQueue = new PriorityQueue<AudioControl.MyStruct>();

	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	@Override
	public String toString() {
		return "AudioControl [priorityQueue=" + priorityQueue + "]";
	}

	public void setAudioData(String audioDataPathUnderAssets, Priority priority) {
		priorityQueue.offer(new MyStruct(audioDataPathUnderAssets, priority));
	}

	public void startAudio() {
		playAudio();
	}

	public void release() {
		mediaPlayer.release();
	}

	private void playAudio() {
		if (mediaPlayer.isPlaying())
			return;
		if (!priorityQueue.isEmpty()) {
			String filepath = priorityQueue.poll().filepath;
			playAudio(filepath);
		}
	}

	private void playAudio(String filepath) {
		try {
			mediaPlayer.reset();
			AssetFileDescriptor afd = context.getAssets().openFd(filepath);
			mediaPlayer.setDataSource(afd.getFileDescriptor(),
					afd.getStartOffset(), afd.getLength());
			mediaPlayer.prepare();
			mediaPlayer.start();
			mediaPlayer
					.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

						@Override
						public void onCompletion(MediaPlayer arg0) {
							playAudio();
						}
					});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}