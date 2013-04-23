package ca.mcgill.mcb.pcingola.snpSift;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import ca.mcgill.mcb.pcingola.interval.Marker;
import ca.mcgill.mcb.pcingola.interval.Markers;
import ca.mcgill.mcb.pcingola.interval.tree.IntervalForest;
import ca.mcgill.mcb.pcingola.util.Gpr;
import ca.mcgill.mcb.pcingola.util.Timer;

/**
 * Intersect intervals
 *  
 * @author pcingola
 */
public class SnpSiftCmdIntersect extends SnpSift {

	class MarkerIntersect implements Cloneable {
		int countIntersects = 0;
		Marker marker;
		HashSet<String> intersected;

		MarkerIntersect(Marker marker) {
			this.marker = marker;
			intersected = new HashSet<String>();
			intersected.add(key(marker));
		}

		MarkerIntersect(Marker marker, MarkerIntersect mi) {
			this.marker = marker;
			this.intersected = new HashSet<String>();
			this.intersected.addAll(mi.intersected);
			this.intersected.add(key(marker));
		}

		boolean done(Marker m) {
			return intersected.contains(key(m));
		}

		MarkerIntersect intersect(Marker m) {
			try {
				Marker mm = marker.intersect(m);
				mm.setId(marker.getId() + "+" + m.getId()); // Update ID
				MarkerIntersect mi = new MarkerIntersect(mm, this);
				mi.intersected.add(key(m));
				return mi;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		String key(Marker m) {
			return m.getChromosomeName() + ":" + m.getStart() + "-" + m.getEnd();
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("Marker: " + marker + "\nIntersected: ");
			for (String i : intersected)
				sb.append(i + "\t");
			sb.append("\n");
			return sb.toString();
		}
	}

	int cluster;
	int minOverlap;
	boolean intersect;
	List<String> fileNames;

	List<IntervalForest> forests;

	public SnpSiftCmdIntersect(String[] args) {
		super(args, "intersect");
	}

	/**
	 * Intersect marker 'm'
	 * @param m
	 * @return
	 */
	Marker intersect(Marker m) {
		HashSet<MarkerIntersect> results = new HashSet<MarkerIntersect>();
		MarkerIntersect mi = new MarkerIntersect(m);
		intersect(mi, results);

		for (MarkerIntersect res : results)
			System.out.println(res);

		return null;
	}

	void intersect(MarkerIntersect mi, HashSet<MarkerIntersect> results) {
		boolean intersect = false;
		for (IntervalForest forest : forests) {
			Markers query = forest.query(mi.marker);

			for (Marker q : query)
				if (!mi.done(q)) {
					intersect(mi.intersect(q), results);
					intersect = true;
				}
		}

		if (!intersect) results.add(mi);
	}

	@Override
	public void parse(String[] args) {
		if (args.length <= 0) usage(null);

		// Defaults
		intersect = true;
		cluster = 2;
		minOverlap = 1;
		fileNames = new ArrayList<String>();

		for (int argc = 0; argc < args.length; argc++) {
			if (isOpt(args[argc])) {
				String arg = args[argc].toLowerCase();
				if (arg.equals("-intersect")) intersect = true;
				else if (arg.equals("-union")) intersect = false;
				else if (arg.equals("-minOverlap")) minOverlap = Gpr.parseIntSafe(args[++argc]);
				else if (arg.equals("-cluster")) cluster = Gpr.parseIntSafe(args[++argc]);
				else usage("Unknown option '" + args[argc] + "'");
			} else fileNames.add(args[argc]);
		}

		// Sanity check
		if (fileNames.size() <= 1) usage("Missing files to intersect");
	}

	/**
	 * Run annotations
	 */
	@Override
	public void run() {
		//---
		// Initialize
		//---
		Markers markersAll = new Markers();

		forests = new ArrayList<IntervalForest>();
		for (String fileName : fileNames) {
			// Load file
			if (verbose) Timer.showStdErr("Loading file '" + fileName + "'");
			Markers markers = Markers.readMarkers(fileName);
			if (verbose) Timer.showStdErr("Done. Marers in file : " + markers.size());

			// Create forest
			IntervalForest ifor = new IntervalForest();
			ifor.add(markers);
			ifor.build();
			forests.add(ifor);
			if (verbose) Timer.showStdErr("Interval forest added.");

			// Add markers to 'all'
			markersAll.add(markers);
		}
		if (verbose) Timer.showStdErr("Total number of markers : " + markersAll.size());

		//---
		// Intersect
		//---
		HashSet<String> done = new HashSet<String>();
		markersAll.sort();
		for (Marker m : markersAll) {
			// Intersect
			Marker mi = intersect(m);

			// Any results? Show them
			if (mi != null) {
				// Output marker data (if not already done)
				String key = mi.getChromosomeName() + "\t" + (mi.getStart() + 1) + "\t" + (mi.getEnd() + 1) + "\t" + mi.getId();
				if (!done.contains(key)) {
					System.out.println(key);
					done.add(key);
				}
			}
		}
	}

	/**
	 * Show usage message
	 * @param msg
	 */
	@Override
	public void usage(String msg) {
		if (msg != null) {
			System.err.println("Error: " + msg);
			showCmd();
		}

		showVersion();

		System.err.println("Usage: java -jar " + SnpSift.class.getSimpleName() + ".jar [options] file_1.bed file_2.bed ... file_N.bed");
		System.err.println("Options:");
		System.err.println("\t-minOverlap <num> : Minimum number of bases that two intervals have to overlap. Default : " + minOverlap);
		System.err.println("\t-cluster <num>    : An interval has to intersect at least 'num' intervals (from other files) to be considered. Default: " + cluster);
		System.err.println("\t-intersect        : Report the intersection of all intervals. Default: " + intersect);
		System.err.println("\t-union            : Report the union of all intervals. Default: " + !intersect);
		System.exit(1);
	}
}