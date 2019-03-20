## Root Controller
`/`

* Usage: list all the endpoints
* Method: Get
* Response body: 

```
{
    "feedback":{
        "href":"https://feedback-analysis-grp-app.herokuapp.com/feedback"
    },
    "feedback_count":{
        "href":"https://feedback-analysis-grp-app.herokuapp.com/feedback/count"
    },
    "feedback_sentiment_count":{
        "href":"https://feedback-analysis-grp-app.herokuapp.com/feedback/sentiments/count"
    },
    "feedback_rating_average":{
        "href":"https://feedback-analysis-grp-app.herokuapp.com/feedback/rating/average"
    },
    "feedback_rating_count":{
        "href":"https://feedback-analysis-grp-app.herokuapp.com/feedback/rating/count"
    },
    "feedback_rating_negative":{
        "href":"https://feedback-analysis-grp-app.herokuapp.com/feedback/rating/negativeperday"
    },
    "feedback_common_phrases":{
        "href":"https://feedback-analysis-grp-app.herokuapp.com/feedback/commonphrases"
    },
    "dashboard":{
        "href":"https://feedback-analysis-grp-app.herokuapp.com/dashboard"
    }
}
```


## Dashboard Controller
`/dashboard`

* Usage: construct a dashboard to display feedbacks and their attributes
* Method: Get
* Request fields:

Parameter|Data Type|Default|Required|Description
:-:|:-:|:-:|:-:|:-:
endpoint|String[]|feedback,feedback_count,feedback_rating_average,feedback_rating_count,feedback_sentiment_count,feedback_rating_negative,feedback_common_phrases|False|endpoints displayed in the dashboard

* Response body:

```
{
    "feedback":Array[24],
    "feedback_rating_average":4.5,
    "feedback_rating_negative":Object{...},
    "endpoints_found":Array[7],
    "endpoints_not_found":Array[0],
    "feedback_common_phrases":Object{...},
    "feedback_rating_count":Object{...},
    "feedback_sentiment_count":Object{...},
    "feedback_count":24
}
```

* Possible error:
   * Code 204, unable to serialize endpoints


## Find All Feedbacks
`/feedback/`

* Usage: list all feedbacks
* Method: Get
* Response body:

```
{
    "feedbackList":[
	{
      "id":"26fabd71-942e-4eda-8d25-e79c487c9259",
  	  "created":"2019-03-13T18:47:41.000+0000",
  	  "rating":4,
  	  "text":"Great app , can make instant payments , and see clearly the transactions. Down side takes 2 days for payment to show you have made and pending transactions are showing same time as though payment has left from account. Always using and checking the app.",
 	    "sentiment":"NEUTRAL",
 	    "stars":"****",
 	    "sentimentEnum":"NEUTRAL",
	    "_links":{
 	       "self":{
          	  "href":"https://feedback-analysis-grp-app.herokuapp.com/feedback/26fabd71-942e-4eda-8d25-e79c487c9259"
  	     },
  	       "feedback":{
        	    "href":"https://feedback-analysis-grp-app.herokuapp.com/feedback"
     	     }
      }
  }，
	{
      "id":"293d7e01-4b80-4434-b73d-c3935367304a",
      "created":"2019-03-13T18:48:28.000+0000",
      "rating":1,
      "text":"Pants!!! won't let me create an account as saying some details are wrong when they are not. Very poor!!!!!",
      "sentiment":"NEUTRAL",
      "stars":"*",
      "sentimentEnum":"NEUTRAL",
      "_links":{
         "self":{
              "href":"https://feedback-analysis-grp-app.herokuapp.com/feedback/293d7e01-4b80-4434-b73d-c3935367304a"
         },
           "feedback":{
              "href":"https://feedback-analysis-grp-app.herokuapp.com/feedback"
           }
      }
  }
    ]
}
```


## Find A Feedback
`/feedback/{id}`

* Usage: find a particular piece of feedback
* Method: Get
* Request fields:

Parameter|Data Type|Default|Required|Description
:-:|:-:|:-:|:-:|:-:
id|String| |True|the id of the feedback needed

* Response body:

```
{
    "id":"26fabd71-942e-4eda-8d25-e79c487c9259",
    "created":"2019-03-13T18:47:41.000+0000",
    "rating":4,
    "text":"Great app , can make instant payments , and see clearly the transactions. Down side takes 2 days for payment to show you have made and pending transactions are showing same time as though payment has left from account. Always using and checking the app.",
    "sentiment":"NEUTRAL",
    "sentimentEnum":"NEUTRAL",
    "stars":"****",
    "_links":{
        "self":{
            "href":"https://feedback-analysis-grp-app.herokuapp.com/feedback/26fabd71-942e-4eda-8d25-e79c487c9259"
        },
        "feedback":{
            "href":"https://feedback-analysis-grp-app.herokuapp.com/feedback"
        }
    }
}
```

* Possible error:
   * Code 404, could not find the feedback of the id


## Create A Feedback
`/feedback`

* Usage: create a new feedback
* Method: Post
* Request fields:

Parameter|Data Type|Default|Required|Description
:-:|:-:|:-:|:-:|:-:
id|String| |False|the id of the new feedback which can be generated automatically
created|Date| |False| the time new feedback created
rating|Integer| |True|the rating of the feedback
text|String| |False|the comment in the feedback
sentiment|String|NEUTRAL|False|the sentiment of the feedback

* Response body:

```
{
    "Created": {
      "href": "https://feedback-analysis-grp-app.herokuapp.com/feedback/26fabd71-942e-4eda-8d25-e79c487c9259"
    }
}
```

* Possible error:
   * Code 400, the reference to store the new feedback could not be found
   * Code 412, the body of the request is not as expected (JSON format with a rating)


## Update A Feedback
`/feedback/{id}`

* Usage: update a feedback
* Method: Put
* Request fields:

Parameter|Data Type|Default|Required|Description
:-:|:-:|:-:|:-:|:-:
id|String| |False|the id of the new feedback which can be generated automatically
created|Date| |False| the time new feedback created
rating|Integer| |True|the rating of the feedback
text|String| |False|the comment in the feedback
sentiment|String|NEUTRAL|False|the sentiment of the feedback
id|String| |True|the id of the feedback to update

* Response body:

```
{
  "href": "https://feedback-analysis-grp-app.herokuapp.com/feedback/26fabd71-942e-4eda-8d25-e79c487c9259"
}
```

* Possible error:
   * Code 400, the reference to store the new feedback could not be found
   * Code 412, the body of the request is not as expected (JSON format with a rating)


## Delete A Feedback
`/feedback/{id}`

* Usage: delete a feedback
* Method: Delete
* Request fields:

Parameter|Data Type|Default|Required|Description
:-:|:-:|:-:|:-:|:-:
id|String| |True|the id of the feedback to be deleted

* Response body:

```
{}
```


## Get Counts
`/feedback/count`

* Usage: get the number of feedbacks
* Method: Get
* Response body:
```
{"count":24}
```

* Possible erroe:
   * Code 204, unable to serialize feedback count


## Get Sentiments Counts
`/feedback/sentiments/count`

* Usage: get the number of sentiments separately
* MEthod: Get
* Response body:

```
{"NEUTRAL":11,"NEGATIVE":6,"POSITIVE":7}
```

* Possible error:
   * Code 204, unable to serialize sentiment counts


## Get Star Ratings Counts
`/feedback/rating/count`

* Usage: get numbers of star ratings
* Method: Get
* Response body:

```
{"1":1,"2":2,"3":0,"4":2,"5":19}
```

* Possible error:
   * Code 204, unable to serialize star rating counts


## Get Average Rating
`/feedback/rating/average`

* Usage: get the average star rating of feedbacks
* Method: Get
* Response body:

```
{"average":4.5}
```

* Possible error:
   * Code 204, unable to serialize average rating


## Get Negative Rating Counts
`/feedback/rating/negativeperday`

* Usage: get number of negative ratings per day
* Method: Get
* Reponse body:

```
{
    "result":[
        {
            "date":1552435200000,
            "volume":3
        },
        {
            "date":1552521600000,
            "volume":1
        },
        {
            "date":1552608000000,
            "volume":2
        }
    ]
}
```

* Possible error:
   * Code 204, unable to serialize negative rating counts


## Get Common Phrases
`/feedback/commonphrases`

* Usage: get common phrases of feedbacks
* Method: Get
* Response body:

```
{
    "result":[
        {
            "term":"easy use",
            "frequency":7
        },
        {
            "term":"show",
            "frequency":3
        }
    ]
}
```

* Possible error:
   * Code 204, unable to serialize average rating
