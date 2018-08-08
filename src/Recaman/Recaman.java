package Recaman;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

public class Recaman {

	private ArrayList<Integer> sequence = new ArrayList<Integer>();
	private ArrayList<Integer> sortedSeq = new ArrayList<Integer>();
	private ArrayList<Integer> unsortedSeq = new ArrayList<Integer>();
	private int complete = 0;

	public void calcSteps(int maxSteps) {

		sequence = new ArrayList<Integer>();
		sortedSeq = new ArrayList<Integer>();
		unsortedSeq = new ArrayList<Integer>();
		complete = 0;

		int fast = maxSteps - maxSteps % 1000;
		int slow = maxSteps % 1000;
		
		int current = 0;
		for (int i = 0; i < fast; i++) {
			if (!unsortedSeq.contains(current - i) && !contains(sortedSeq, current - i) && current - i > 0) {
				unsortedSeq.add(current -= i);
			} else {
				unsortedSeq.add(current += i);
			}
			
			if(unsortedSeq.size() % 1000 == 0) {
				// check for the completed part of the list
				for (int j = complete; j < sequence.size(); j++) {
					if (!sequence.contains(j)) {
						complete = j - 1;
						break;
					}
				}
				sequence.addAll(unsortedSeq);
				sortedSeq.addAll(unsortedSeq);
				Collections.sort(sortedSeq);
				unsortedSeq = new ArrayList<Integer>();
			}
		}
		for (int i = 0; i < slow; i++) {
			if (!sequence.contains(current - i) && current - i > 0) {
				sequence.add(current -= i);
			} else {
				sequence.add(current += i);
			}
		}
		for (int j = complete; j < sequence.size(); j++) {
			if (!sequence.contains(j)) {
				complete = j - 1;
				break;
			}
		}
	}

	public void calcComplete(int minComplete) {
		calcComplete(minComplete, true);
	}
	
	public void calcComplete(int minComplete, boolean print) {
		
		sequence = new ArrayList<Integer>();
		sortedSeq = new ArrayList<Integer>();
		unsortedSeq = new ArrayList<Integer>();
		complete = 0;
		
		int current = 0;
		int count = 0;
		long lastTime = System.currentTimeMillis();
		
		while (complete < minComplete) {
			if (!unsortedSeq.contains(current - count) && !contains(sortedSeq, current - count) && current - count > 0) {
				unsortedSeq.add(current -= count);
			} else {
				unsortedSeq.add(current += count);
			}
			count++;
			
			if(unsortedSeq.size() % 1000 == 0) {
				// check for the completed part of the list
				for (int i = complete; i < sequence.size(); i++) {
					if (!sequence.contains(i)) {
						complete = i - 1;
						break;
					}
				}

				if(print && sequence.size() % 10000 == 0) {
					System.out.println("Length: " + sequence.size() + "\tComplete: " + complete + "\tTime: " + (System.currentTimeMillis()-lastTime) + " ms");
					lastTime = System.currentTimeMillis();
				}
				sequence.addAll(unsortedSeq);
				sortedSeq.addAll(unsortedSeq);
				Collections.sort(sortedSeq);
				unsortedSeq = new ArrayList<Integer>();
			}
		}
	}
	
	private boolean contains(List<Integer> list, int number) {
		if(list.size() < 10)
			return list.contains(number);
		
		if(number < list.get(list.size()/2))
			return contains(list.subList(0, list.size()/2), number);
		else
			return contains(list.subList(list.size()/2, list.size()), number);
	}

	public void print() {
		sequence.forEach(i -> System.out.println(i));
	}

	public void toFile(String filename) {
		try {
			PrintWriter writer = new PrintWriter(filename + ".txt", "UTF-8");
			sequence.forEach(i -> writer.print(i + ", "));
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final int DEFAULT_X = 0;
	private final int DEFAULT_RATIO_X = 16;
	private final int DEFAULT_RATIO_Y = 9;
	private final int DEFAULT_SCALE = 2;
	
	public void toImage() {
		toImage(DEFAULT_X, DEFAULT_RATIO_X, DEFAULT_RATIO_Y, DEFAULT_SCALE);
	}
	public void toImage(int scale) {
		toImage(DEFAULT_X, DEFAULT_RATIO_X, DEFAULT_RATIO_Y, scale);
	}
	public void toImage(int x, int scale) {
		toImage(x, DEFAULT_RATIO_X, DEFAULT_RATIO_Y, scale);
	}
	public void toImage(int ratioX, int ratioY, int scale) {
		toImage(DEFAULT_X, ratioX, ratioY, scale);
	}
	
	public void toImage(int x, int ratioX, int ratioY, int scale) {
		if(sequence == null || sequence.size() == 0) return;
		int width = (x != 0 ? x / scale : (complete != 0 ? complete : sequence.size())) * scale;
		int height = width / ratioX * ratioY;
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D cg = bi.createGraphics();
		cg.setRenderingHints(new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON));
		
		ArrayList<int[]> toDraw = new ArrayList<int[]>();
		int now = sequence.get(0);
		for(int i = 1; i < sequence.size(); i++) {
			int next = sequence.get(i);
			toDraw.add(new int[]{
					((now < next ? next : now) - i) * scale, height/2 - i*scale/2,
					i * scale, i * scale,
					(i % 2 == 0 ? 180 : 0), 180});
			now = next;
		}
		toDraw.removeIf(arc -> arc[0] > width);
		Collections.reverse(toDraw);
		System.out.println("Arcs to draw: " + toDraw.size());
		int count = 0;
		for(int[] arc : toDraw) {
			switch(count % 7) {
			case 0:
				cg.setColor(Color.CYAN);
				break;
			case 1:
				cg.setColor(Color.BLUE);
				break;
			case 2:
				cg.setColor(Color.GREEN);
				break;
			case 3:
				cg.setColor(Color.YELLOW);
				break;
			case 4:
				cg.setColor(Color.RED);
				break;
			case 5:
				cg.setColor(Color.MAGENTA);
				break;
			case 6:
				cg.setColor(Color.ORANGE);
				break;
			}
			count++;
			cg.fillArc(arc[0], arc[1], arc[2], arc[3], arc[4], arc[5]);
			cg.setColor(Color.BLACK);
			cg.setStroke(new BasicStroke(4));
			cg.drawArc(arc[0]-1, arc[1], arc[2], arc[3], arc[4], arc[5]);
		}
		
		try {
			ImageIO.write(bi, "png", new File(width + "x" + height + " scale " + scale + ".png"));
			System.out.println("Done! :)");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Integer> getSequence() {
		return sequence;
	}
	
	public int getComplete() {
		return complete;
	}
	
	public int getSteps() {
		return sequence.size();
	}
}
