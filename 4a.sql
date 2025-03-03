create database hashtagas;
use hashtagas;
CREATE TABLE IF NOT EXISTS Tweets (
    tweet_id INT PRIMARY KEY AUTO_INCREMENT,
    tweet_date DATE NOT NULL,
    tweet TEXT NOT NULL
);
INSERT INTO Tweets (tweet_date, tweet) VALUES
('2024-02-17', 'I love #coding and #AI!'),
('2024-02-19', '#AI is the future of #technology'),
('2024-02-21', 'Learning #MySQL and #database management'),
('2024-02-23', 'Building a #database project with #MySQL'),
('2024-02-25', '#AI is revolutionizing the world!'),
('2024-02-27', 'Just finished a #coding challenge! #fun');


WITH RECURSIVE Hashtags AS (
    -- Extract the first hashtag from each tweet
    SELECT 
        tweet_id, 
        tweet_date, 
        SUBSTRING_INDEX(SUBSTRING_INDEX(tweet, '#', 2), ' ', 1) AS hashtag,
        SUBSTRING_INDEX(tweet, ' ', -1) AS remaining_tweet,
        1 AS depth
    FROM Tweets
    WHERE tweet_date BETWEEN '2024-02-01' AND '2024-02-29'
    
    UNION ALL

    -- Recursively extract the next hashtag
    SELECT 
        tweet_id, 
        tweet_date, 
        SUBSTRING_INDEX(SUBSTRING_INDEX(remaining_tweet, '#', 2), ' ', 1),
        SUBSTRING_INDEX(remaining_tweet, ' ', -1),
        depth + 1
    FROM Hashtags
    WHERE remaining_tweet LIKE '%#%'
    AND depth < 10  -- Prevent infinite recursion
),

HashtagCounts AS (
    -- Count occurrences of each hashtag
    SELECT hashtag, COUNT(*) AS count
    FROM Hashtags
    WHERE hashtag LIKE '#%'  -- Ensure only hashtags are counted
    GROUP BY hashtag
)

-- Get the top 3 most frequent hashtags
SELECT hashtag, count
FROM HashtagCounts
ORDER BY count DESC, hashtag ASC
LIMIT 3;
