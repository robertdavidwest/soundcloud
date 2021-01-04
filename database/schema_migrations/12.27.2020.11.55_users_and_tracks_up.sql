BEGIN; 

CREATE SCHEMA soundcloud; 

CREATE TABLE soundcloud.users (
    user_id serial PRIMARY KEY,
    soundcloud_user_id INTEGER UNIQUE, -- id in json
    username TEXT, 
    first_name TEXT,
    full_name TEXT, 
    last_name TEXT,

    avatar_url TEXT, 
    city TEXT, 
    country_code TEXT,
    created_at TEXT, 
    description TEXT, 
    urn TEXT,
    uri TEXT,
    permalink TEXT,
    permalink_url TEXT,
    visual_url TEXT
);

CREATE TABLE soundcloud.users_timeline (
   users_timeline_id serial PRIMARY KEY,
   user_id INTEGER, 

   pull_date TIMESTAMP DEFAULT NOW(),

   comments_count INTEGER, 
   followers_count INTEGER, 
   followings_count INTEGER,
   likes_count INTEGER, 
   groups_count INTEGER, 
   playlist_likes_count INTEGER,
   playlist_count INTEGER, 
   reposts_count INTEGER, 
   track_count INTEGER,
   FOREIGN KEY (user_id) REFERENCES soundcloud.users (user_id)
);

CREATE TABLE soundcloud.tracks (
    track_id serial PRIMARY KEY, 
    user_id serial,
    soundcloud_track_id INTEGER, -- id in json
    title TEXT,
    genre TEXT, 
    duration INTEGER, 
    track_format TEXT, 
    tag_list TEXT, 
    created_at TEXT,
    
    steamable BOOLEAN,     
    commentable BOOLEAN, 
    downloadable BOOLEAN,
    had_downloads_left BOOLEAN, 

    description TEXT, 
    urn TEXT,
    uri TEXT,
    permalink TEXT,
    permalink_url TEXT,
    artwork_url TEXT,
    waveform_url TEXT
); 

CREATE TABLE soundcloud.tracks_timeline (
    track_timeline_id serial PRIMARY KEY,
    track_id INTEGER,
    user_id INTEGER,
    pull_date TIMESTAMP DEFAULT NOW(),
    comment_count INTEGER,
    playback_count INTEGER,
    likes_count INTEGER, 
    download_count INTEGER,
    reposts_count INTEGER,
    FOREIGN KEY (track_id) REFERENCES soundcloud.tracks (track_id),
    FOREIGN KEY (user_id) REFERENCES soundcloud.users (user_id)
); 

-- from json field tag_list in track data 
CREATE TABLE soundcloud.track_tags (
    tag_id serial PRIMARY KEY,
    track_id INTEGER,
    tag TEXT,
    FOREIGN KEY (track_id) REFERENCES soundcloud.tracks (track_id)
);

COMMIT;
ROLLBACK;
