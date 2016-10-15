import requests
import json
import time
import sys
reload (sys)
sys.setdefaultencoding('utf8')

from requests_oauthlib import OAuth1

# authentication pieces
client_key    = "" # fill in
client_secret = "" # fill in
token         = "" # fill in
token_secret  = "" # fill in

# the base for all Twitter calls
base_twitter_url = "https://api.twitter.com/1.1/"

# setup authentication
oauth = OAuth1(client_key,client_secret,token,token_secret)

#
# Download Tweets from a user profile
#
def download_tweets(screen_name,number_of_tweets,max_id=None):
    
    api_url  = "%sstatuses/user_timeline.json?" % base_twitter_url
    api_url += "screen_name=%s&" % screen_name
    api_url += "count=%d" % number_of_tweets
    if max_id is not None:
        api_url += "&max_id=%d" % max_id

    # send request to Twitter
    response = requests.get(api_url,auth=oauth)
    
    if response.status_code == 200:
        
        tweets = json.loads(response.content)
        
        return tweets
    

    return None

#
# Takes a username and begins downloading all Tweets
#
def download_all_tweets(username,logger):
    full_tweet_list = []
    max_id          = 0
    
    # grab the first 200 Tweets
    tweet_list   = download_tweets(username,200)
    # grab the oldest Tweet
    if len(tweet_list)==0:
        t="No Tweets Found for %s\n"%username
        print t
        logger.AppendText(t)
        return None
    else:
        oldest_tweet = tweet_list[-1]
        
        # continue retrieving Tweets
        while max_id != oldest_tweet['id']:
        
            full_tweet_list.extend(tweet_list)
    
            # set max_id to latest max_id we retrieved
            max_id = oldest_tweet['id']         
    
            text= "[*] Retrieved: %d Tweets (max_id: %d)\n" % (len(full_tweet_list),max_id)
            print text
            logger.AppendText(text)
            # sleep to handle rate limiting
            time.sleep(3)
            
            # send next request with max_id set
            tweet_list = download_tweets(username,200,max_id-1)
        
            # grab the oldest Tweet
            if len(tweet_list):
                oldest_tweet = tweet_list[-1]
            
    
        # add the last few Tweets
        full_tweet_list.extend(tweet_list)
        text ="[*]Total Retrieved: %d Tweets\n" % (len(full_tweet_list))    
        logger.AppendText(text)
        # return the full Tweet list
        return full_tweet_list 
    
def print_all_tweets(usrn,logger):
    full_tweet_list = download_all_tweets(usrn,logger)
    array =[[],[],[]]
    
    strn = ""
    if full_tweet_list is not None:
        for tweet in full_tweet_list:
            tweet_text = "%s \t %s \t %s\n\n" % (tweet['created_at'],tweet['text'],str(tweet['geo']))
            array[0].append(tweet['created_at'].encode("utf-8"))
            array[1].append(tweet['text'].encode("utf-8"))
            array[2].append(str(tweet['geo']).encode("utf-8"))
            tweet_text = tweet_text.encode("utf-8")
            print tweet_text
            strn += tweet_text
        strn = strn.encode("utf8")
        #print strn 
        ##return strn
        return array
    else:
        return None
#usrn = "bellingcat"
#print_all_tweets(usrn)
# loop over each Tweet and print the date and text
