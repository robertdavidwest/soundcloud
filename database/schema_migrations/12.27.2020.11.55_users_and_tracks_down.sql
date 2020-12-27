BEGIN; 

DROP TABLE soundcloud.track_tags;
DROP TABLE soundcloud.tracks_timeline;
DROP TABLE soundcloud.tracks; 
DROP TABLE soundcloud.users_timeline;
DROP TABLE soundcloud.users;
DROP SCHEMA soundcloud; 

COMMIT;
ROLLBACK;
