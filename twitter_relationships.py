import requests
import json
import time
import sys
import pickle
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
# Main Twitter API function for sending requests
#
def send_request(screen_name,relationship_type,next_cursor=None):

    
    url = "https://api.twitter.com/1.1/%s/ids.json?screen_name=%s&count=5000" % (relationship_type,username)
    
    if next_cursor is not None:
        url += "&cursor=%s" % next_cursor
        
    response = requests.get(url,auth=oauth)
    
    time.sleep(3)
    
    if response.status_code == 200:
        
        result = json.loads(response.content)
        
        return result
    
    return None


#
# Function that contains the logic for paging through results
#
def get_all_friends_followers(username,relationship_type):
    
    account_list = []
    next_cursor  = None
    
    # send off first request
    accounts = send_request(username,relationship_type)
    
    # valid user account so start pulling relationships
    if accounts is not None:
        
        account_list.extend(accounts['ids'])
        
        print "[*] Downloaded %d of type %s" % (len(account_list),relationship_type)

        
        # while we have a cursor keep downloading friends and followers
        while accounts['next_cursor'] != 0 and accounts['next_cursor'] != -1:

            accounts = send_request(username,relationship_type,accounts['next_cursor'])
            
            if accounts is not None:
                
                account_list.extend(accounts['ids'])
                print "[*] Downloaded %d of type %s" % (len(account_list),relationship_type)

            else:
                
                break
    
     
    return account_list

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
        for user in result:
            if user_type =="username":
                id = user.get('id')
            else:
                id = user.get("screen_name")
            #print id
        return result

    #print "None"
    return None

#RUN

username = "bellingcat"

friends   = get_all_friends_followers(username,"friends")
followers = get_all_friends_followers(username,"followers") 


print "[**] Retrieved %d friends" % len(friends)
print "[**] Retrieved %d followers" % len(followers)


snapshot_timestamp = time.time()

# /store the friends

fd = open("%s-friends.pkl" % username,"wb")
pickle.dump(friends, fd)
fd.close()
print "[!] Stored friends in %s-%f-friends.pkl" % (username,snapshot_timestamp)

#/ store the followers
fd = open("%s-followers.pkl" % username,"wb")
pickle.dump(followers,fd)
fd.close()
print "[!] Stored followers in %s-%f-followers.pkl" % (username,snapshot_timestamp)
