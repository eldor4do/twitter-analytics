add jar /path/to/json-serde-1.3-jar-with-dependencies.jar;
create table tweets (
   created_at string,
   entities struct <
      hashtags: array <struct<text: string>>,
      media: array <struct <
            media_url: string,
            media_url_https: string,
            sizes: array < struct <url: string>>>>,
      urls: array <struct<
            url: string>>,
      user_mentions: array <struct<
            name: string,
            screen_name: string>>>,
   geo struct <
      coordinates: array <struct<
      type: string>>>,
   id bigint,
   id_str string,
   in_reply_to_screen_name string,
   in_reply_to_status_id bigint,
   in_reply_to_status_id_str string,
   in_reply_to_user_id int,
   in_reply_to_user_id_str string,
   retweeted_status struct <
      created_at: string,
      entities: struct <
         hashtags: array <struct<
               text: string>>,
         media: array <struct<
               media_url: string,
               media_url_https: string,
               sizes: array <struct<url: string>>>>,
         urls: array <struct<
               url: string>>,
         user_mentions: array <struct<
               name: string,
               screen_name: string>>>,
      geo: struct <
         coordinates: array <struct<type: string>>>,
      id: bigint,
      id_str: string,
      in_reply_to_screen_name: string,
      in_reply_to_status_id: bigint,
      in_reply_to_status_id_str: string,
      in_reply_to_user_id: int,
      in_reply_to_user_id_str: string,
      source: string,
      text: string ,
      user_name: struct <
         id: int,
         id_str: string,
         name: string,
         profile_image_url_https: string,
         protected: boolean,
         screen_name: string,
         verified: boolean>>,
   source string,
   text string,
   user_n struct <
      id: int,
      id_str: binary,
      name: string,
      profile_image_url_https: string,
      protected: boolean,
      screen_name: string,
      verified: boolean>
)
ROW FORMAT SERDE 'org.openx.data.jsonserde.JsonSerDe'
STORED AS TEXTFILE;

add jar /home/eldor4do/Documents/TwitterPython/json-serde-1.3-jar-with-dependencies.jar;
load data local inpath '/home/eldor4do/Documents/TwitterPython/all_tweets.json' overwrite into table tweets;
