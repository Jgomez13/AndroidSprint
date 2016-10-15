from tweepy.streaming import StreamListener
from tweepy import OAuthHandler
from tweepy import Stream

import json
import codecs
import requests
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
base_twitter_url = "https://api.twitter.com/1.1/users/lookup.json"

# setup authentication
oauth = OAuth1(client_key,client_secret,token,token_secret)

class StdOutListener(StreamListener):

    # 
    # Receives messages as they come in from Twitter
    #
    def on_data(self, data):

        # convert from JSON to a dictionary
        tweet = json.loads(data)

        # log the Tweet to a file
        fd = codecs.open("tweets.log","ab",encoding="utf-8")
        fd.write("%s\r\n" % tweet)
        fd.close()

        # print the tweet out
        tweet_text = "%s, %s, \"%s\"" % (tweet['created_at'],tweet['user']['screen_name'],tweet['text'])
        tweet_text.encode("utf-8")
        print tweet_text

        return True

    #
    # Receives error messages from the Twitter API
    #
    def on_error(self, status):
        print "[!] ERROR: %s" % status

def convert_username(user, user_type):
    api_url = "https://api.twitter.com/1.1/users/lookup.json?"
    
    if user_type == "username":
        api_url+= "screen_name=%s" % user
    else:
        api_url += "user_id=%s" % user
    # now make the request 
    response = requests.get(api_url,auth=oauth) 
    #test for 200 status code
    if response.status_code == 200:           
        #parse json
        result = json.loads(response.content)
        ids = []
        for user in result:
            usrn = user.get('screen_name')
            id = user.get('id')
            
            ids.append(("%d" % id))
            print "username: %s \tID: %d" % (usrn,id)
        return ids
        
    print "None"
    return None
            

l = StdOutListener()
auth = OAuthHandler(client_key, client_secret)
auth.set_access_token(token, token_secret)
#ids = convert_username("diggupdates, deliciousrecent, imdb, twitseeker, rosehose, ladyreporter, nieuwslijstnl, dogbook, combatsi",'username')

#stream = Stream(auth, l)
#stream.filter(follow=ids)
