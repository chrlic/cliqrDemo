package cz.gargoyle.cliqr.demo.fe

class CpuCruncher {
	public static void crunch(long ms) {
		long startTime = System.currentTimeMillis()
		long counter = 0
		long iterations = 0
		while ((startTime + ms > System.currentTimeMillis()) || (iterations < ms * 1000)) {
			counter = (counter + 997) % 9999999
			iterations++
		}
		//println iterations
	}
}
