### Functional Requirements:
- Store information related to videos.
- Stream video content.
- Keeps track of user engagement actions related to videos.


### To perform the above functionality, you are expected to implement an API which would allow users to:
- Publish a video
- Add and Edit the metadata associated with the video (some examples of metadata are: Title, Synopsis, Director, Cast, Year of Release, Genre, Running time)
- Delist (soft delete) a video and its associated metadata
- Load a video – return the video metadata and the corresponding content.
- Play a video – return the content related to a video. The content can be a simple string that acts as a mock to the actual video content.
- List all available videos – This should return only a subset of the video metadata such as: Title, Director, Main Actor, Genre and Running Time.
- Search for videos based on some search/query criteria (e.g.: Movies directed by a specific director). The returned result-set should still feature the same subset of metadata as outlined in the previous point.
- Retrieve the engagement statistic for a video. Engagement can be split in 2: Impressions (A client loading a video), Views (A client playing a video).
