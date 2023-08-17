package MonteCarloMini;
/* Parallel  program to use Monte Carlo method to 
 * locate a minimum in a function
 * Thabang Sambo, University of Cape Town
 */
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

class MinimizationParallel extends RecursiveTask<Integer> {//recursive task
   private final int stFinder;//index 0
   private final int topFinder;//ending index
   private final SearchParallel[] searches;//store in array
   private final int threshold;//to adjust the workload and imbalance
   
   public MinimizationParallel(int stFinder, int topFinder, SearchParallel[] searches, int threshold) {
      this.stFinder = stFinder;//index 0
      this.topFinder = topFinder;//end index
      this.searches = searches;//search
      this.threshold = threshold;//workload
   }
   
   //all searches
   @Override
   protected Integer compute() {
      int min = Integer.MAX_VALUE;
      int local_min = Integer.MAX_VALUE;
      int finder = -1;//initialize
      
      if (topFinder - stFinder <= threshold) {//don't go beyond treshold
         for (int i = stFinder; i < topFinder; i++) {
            local_min = searches[i].find_valleys();
            if ((!searches[i].isStopped()) && (local_min < min)) {//don't look at  those who stopped because hit exisiting path
               min = local_min;
               finder = i;//keep track of who found it
            }
         }
      } else {
         int midFinder = stFinder + (topFinder - stFinder) / 2;
         MinimizationParallel SearchTaskLeft = new MinimizationParallel(stFinder, midFinder, searches, threshold);
         MinimizationParallel SearchTaskRight = new MinimizationParallel(midFinder, topFinder, searches, threshold);
         
         invokeAll(SearchTaskLeft, SearchTaskRight);//invoke the sub tasks
         
         int MinimumValueLeft = SearchTaskLeft.join();//join values left
         int MinimumValueRight = SearchTaskRight.join();//join values right
         
         if (MinimumValueLeft < MinimumValueRight) {//compare
            min = MinimumValueLeft;
         } else {
            min = MinimumValueRight;
         }
      }
      
      return min;//return the value determined
   }
}

//main
public class MonteCarloMinimizationParallel {
   static final boolean DEBUG = false;
   
   static long startTime = 0;
   static long endTime = 0;
//timers - note milliseconds
   private static void tick() {
      startTime = System.currentTimeMillis();
   }
   private static void tock() {
      endTime = System.currentTimeMillis(); 
   }

   public static void main(String[] args) {
      int rows, columns;//grid size
      double xmin, xmax, ymin, ymax; //x and y terrain limits
      TerrainArea terrain; //object to store the heights and grid points visited by searches
      double searches_density;// Density - number of Monte Carlo  searches per grid position
      int num_searches;// Number of searches
      SearchParallel[] searches;// Array of searches
      Random rand = new Random();//the random number generator
      //handle error input
      if (args.length != 7) {
         System.out.println("Incorrect number of command line arguments provided.");
         System.exit(0);
      }
      /* Read argument values */
      rows = Integer.parseInt(args[0]);
      columns = Integer.parseInt(args[1]);
      xmin = Double.parseDouble(args[2]);
      xmax = Double.parseDouble(args[3]);
      ymin = Double.parseDouble(args[4]);
      ymax = Double.parseDouble(args[5]);
      searches_density = Double.parseDouble(args[6]);
      
      if (DEBUG) {
      /* Print arguments */
         System.out.println("Arguments, Rows: " + rows + ", Columns: " + columns);
         System.out.println("Arguments, x_range: ( " + xmin + ", " + xmax + " ), y_range( " + ymin + ", " + ymax + " )");
         System.out.println("Arguments, searches_density: " + searches_density + "\n");
      }
      /* Initialize  */
      terrain = new TerrainArea(rows, columns, xmin, xmax, ymin, ymax);
      num_searches = (int) (rows * columns * searches_density);
      searches = new SearchParallel[num_searches];
      for (int i = 0; i < num_searches; i++) {
         searches[i] = new SearchParallel(i + 1, rand.nextInt(rows), rand.nextInt(columns), terrain);
      }
      /* Print initial values */
      if (DEBUG) {
         System.out.println("Number searches: " + num_searches);
      }
      
      int threshold = 1000; // Set your desired threshold value
      
      ForkJoinPool forkJoinPool = new ForkJoinPool(); //create fork join object
      
      tick();//start timer
      //parallel search declaration
      MinimizationParallel searchParallel = new MinimizationParallel(0, num_searches, searches, threshold);
      int globalMininimumValue = forkJoinPool.invoke(searchParallel);//global minimum
      int finder = 0;//index
      
      tock();//end
      
      if (DEBUG) {
         // Print final state...
         terrain.print_heights();
         terrain.print_visited();
      }
      
      //check who found minimum and assign index
      for (int i = 0; i < num_searches; i++) {
         if ((!searches[i].isStopped()) && (searches[i].find_valleys() == globalMininimumValue)) {
            finder = i;
            break;
         }
      }
   
      //Parameters
      System.out.println("Run parameters");
      System.out.println("\t Rows: " + rows + ", Columns: " + columns);
      System.out.println("\t x: [" + xmin + ", " + xmax + "], y: [" + ymin + ", " + ymax + "]");
      System.out.println("\t Search density: " + searches_density + " (" + num_searches + " searches)");
      /*  Total computation time */
      System.out.println("Time: " + (endTime - startTime) + " ms");
      int tmp = terrain.getGrid_points_visited();
      System.out.printf("Grid points visited: %d  (%2.0f%s)\n",tmp,(tmp/(rows*columns*1.0))*100.0, "%");
      tmp = terrain.getGrid_points_evaluated();
      System.out.printf("Grid points evaluated: %d  (%2.0f%s)\n",tmp,(tmp/(rows*columns*1.0))*100.0, "%");
      /* Results*/
      System.out.printf("Global minimum: %d at x=%.1f y=%.1f\n\n"  , globalMininimumValue ,terrain.getXcoord(searches[finder].getPos_row()) , terrain.getYcoord(searches[finder].getPos_col()));
   }
}


