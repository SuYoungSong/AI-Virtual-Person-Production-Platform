package untitle.endproject.demonstration.repository;

import org.springframework.stereotype.Repository;
import untitle.endproject.demonstration.domain.Audio;
import untitle.endproject.demonstration.domain.Image;
import untitle.endproject.demonstration.domain.Video;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MemoryRepository {
    private static final Map<Long, Image> imageMap = new ConcurrentHashMap<>();
    private static final Map<Long, Audio> audioMap = new ConcurrentHashMap<>();
    private static final Map<Long, Video> videoMap = new ConcurrentHashMap<>();

    private static long imageCount = 0L;
    private static long audioCount = 0L;
    private static long videoCount = 0L;



}
