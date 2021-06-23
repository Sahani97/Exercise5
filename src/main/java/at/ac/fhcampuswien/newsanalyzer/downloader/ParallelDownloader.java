package at.ac.fhcampuswien.newsanalyzer.downloader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParallelDownloader extends Downloader{
    @Override
    public int process(List<String> urls) {
        int count = 0;

        int availableProcessors = Runtime.getRuntime().availableProcessors();

        ExecutorService pool = Executors.newFixedThreadPool(availableProcessors);

        List<Callable<String>> callables = new ArrayList<>();
        for(int i = 0; i < urls.size(); i++){
            int idx = i;
            Callable<String> task = () -> saveUrl2File(urls.get(idx));
            callables.add(task);
        }

        try {
            List<Future<String>> allFutures = pool.invokeAll(callables);
            for(Future<String> f : allFutures){
                String result = f.get();
                if(result != null)
                    count++;
            }
        } catch (Exception e) {
            System.out.println("Error occured");
        }
        pool.shutdown();
        return count;
    }
}