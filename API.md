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


## Find All Dashboards
`/dashboards`

* Usage: List all dashboards
* Method: Get
* Response body:

```
{
    "dashboardList":[  
         {  
            "id":"d8dbc176-8ba0-41a7-a777-3734af30f8ca",
            "name":"MobileAppDashboard"
         },
         {  
            "id":"e99f1a5e-5666-4f35-a08b-190aeeb2d0db",
            "name":"AppDashboard"
         }
      ]
}
```


## Create A Dashboard
`/dashboards/`

* Usage: create a dashboard
* Method: Post
* Request body:

```
{
    "id":"...",
    "name":"..."
}
```

* Response body:

```
{
    "Created": {
      "href": "https://feedback-analysis-grp-app.herokuapp.com/dashboards/e99f1a5e-5666-4f35-a08b-190aeeb2d0db"
    }
}
```

* Possible error:
   * Code 400, the reference to store the dashboard could not be found
   * Code 412, the body of the request is not as expected


## Update A Dashboard
`/dashboards/{dashboardId}`

* Usage: update a dashboard
* Method: Put
* URL parameter: 

Parameter|Data Type|Default|Required|Description
:-:|:-:|:-:|:-:|:-:
dashboardId|String| |True|the id of the dashboard to update

* Request body:

{
    "id":"...",
    "name":"..."
}

* Response body:

```
{
      "href": "https://feedback-analysis-grp-app.herokuapp.com/dashboards/e99f1a5e-5666-4f35-a08b-190aeeb2d0db"
}
```

* Possible error:
   * Code 400, the reference to store the new dashboard could not be found
   * Code 412, the body of the request is not as expected


## Delete A Dashboard
`/dashboards/{dashboardId}`

* Usage: delete a dashboard
* Method: Delete
* URL parameter:

Parameter|Data Type|Default|Required|Description
:-:|:-:|:-:|:-:|:-:
id|String| |True|the id of the dashboard to delete

* Response body:

```
{}
```

* Possible error:
   * Code 404, could not find the dashboard of the id


## Find A Feedback
`/feedback/{id}`

* Usage: find a particular piece of feedback
* Method: Get
* URL parameter:

Parameter|Data Type|Default|Required|Description
:-:|:-:|:-:|:-:|:-:
id|String| |True|the id of the feedback

* Response body:

```
{
    "id": "d1df6785-b82b-43fd-950a-20771bcf6dcd",
    "dashboardId": "e99f1a5e-5666-4f35-a08b-190aeeb2d0db",
    "created": "2019-04-05T07:43:04.000+0000",
    "rating": 5,
    "text": "must be simple because I can use it no problem!\n",
    "sentiment": "NEUTRAL",
    "stars": "*****",
    "sentimentEnum": "NEUTRAL",
    "_links": {
        "self": {
            "href": "https://feedback-analysis-grp-app.herokuapp.com/feedback/d1df6785-b82b-43fd-950a-20771bcf6dcd"
        },
        "feedback": {
            "href": "https://feedback-analysis-grp-app.herokuapp.com/feedback?dashboardId=e99f1a5e-5666-4f35-a08b-190aeeb2d0db"
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
* Request body:

```
{
    "dashboardId":"...",
    "rating":"..."
    "text":"..."
}
```

* Response body:

```
{
    "Created": {
      "href": "https://feedback-analysis-grp-app.herokuapp.com/feedback/26fabd71-942e-4eda-8d25-e79c487c9259"
    }
}
```

* Possible error:
   * Code 400, unable to parse the feedback
   * Code 412, the body of the request is not as expected (JSON format with a rating)


## Update A Feedback
`/feedback/{id}`

* Usage: update a feedback
* Method: Put
* URL parameter:

Parameter|Data Type|Default|Required|Description
:-:|:-:|:-:|:-:|:-:
id|String| |True|the id of the feedback to update

* Request body:

```
{
    "dashboardId":"...",
    "rating":"..."
    "text":"..."
}
```

* Response body:

```
{
  "href": "https://feedback-analysis-grp-app.herokuapp.com/feedback/26fabd71-942e-4eda-8d25-e79c487c9259"
}
```

* Possible error:
   * Code 400, unable to parse the feedback
   * Code 412, the body of the request is not as expected (JSON format with a rating)


## Delete A Feedback
`/feedback/{id}`

* Usage: delete a feedback
* Method: Delete
* URL parameter:

Parameter|Data Type|Default|Required|Description
:-:|:-:|:-:|:-:|:-:
id|String| |True|the id of the feedback to be deleted

* Response body:

```
{}
```

* Possible error:
   * Code 400, the reference to store the new feedback could not be found
   * Code 404, could not find the feedback of the id


## Find All Feedback
`/feedback/`

* Usage: find all feedback of a dashboard
* Method: Get
* Query string:
`/feedback?dashboardId=...`

* Response body:

```
feedbackList:[
    {
        "id": "d1df6785-b82b-43fd-950a-20771bcf6dcd",
        "dashboardId": "e99f1a5e-5666-4f35-a08b-190aeeb2d0db",
        "created": "2019-04-05T07:43:04.000+0000",
        "rating": 5,
        "text": "must be simple because I can use it no problem!\n",
        "sentiment": "NEUTRAL",
        "stars": "*****",
        "sentimentEnum": "NEUTRAL",
        "_links": {
            "self": {
                "href": "https://feedback-analysis-grp-app.herokuapp.com/feedback/d1df6785-b82b-43fd-950a-20771bcf6dcd"
            },
            "feedback": {
                "href": "https://feedback-analysis-grp-app.herokuapp.com/feedback?dashboardId=e99f1a5e-5666-4f35-a08b-190aeeb2d0db"
            }
        }
    },
    {
        "id": "41531f6d-45e9-4870-8067-2114336d6f97",
        "dashboardId": "e99f1a5e-5666-4f35-a08b-190aeeb2d0db",
        "created": "2019-04-05T07:41:28.000+0000",
        "rating": 5,
        "text": "excellent app easy to use and updated very often to give accurate balances. I love the fingerprint login as I forget my pin numbers. 5 stars",
        "sentiment": "POSITIVE",
        "stars": "*****",
        "sentimentEnum": "POSITIVE",
        "_links": {
            "self": {
                "href": "https://feedback-analysis-grp-app.herokuapp.com/feedback/41531f6d-45e9-4870-8067-2114336d6f97"
            },
            "feedback": {
                "href": "https://feedback-analysis-grp-app.herokuapp.com/feedback?dashboardId=e99f1a5e-5666-4f35-a08b-190aeeb2d0db"
            }
        }
    }
]
```

* Possible error:
   * Code 400, no dashboard id specified


## Get Feedback's status
`/feedback/stats`

* Usage: get a dashboard's feedback's overall status
* Method: Get
* Query string:
`/feedback/stats?dashboardId=...`

* Response body:

```
{
    "feedback_paged":Array[1],
    "feedback_rating_average":5,
    "dashboard_name":"AppDashboard",
    "feedback_rating_negative":Object{...},
    "feedback_common_phrases":Object{...},
    "feedback_rating_count":Object{...},
    "feedback_sentiment_count":Object{...},
    "feedback_count":2
}
```

* Possible errors:
   * Code 204, unable to serialize endpoints


## Get Feedback paginated
`/feedback/paged`

* Usage: get paginated feedback
* Method: Get
* Query string:
`/feedback/paged?dashboardId=...&page=...&pageSize=...`

* Response body:

```
{
    "_embedded": {
        "feedbackList": [
            {
                "id": "d1df6785-b82b-43fd-950a-20771bcf6dcd",
                "dashboardId": "e99f1a5e-5666-4f35-a08b-190aeeb2d0db",
                "created": "2019-04-05T07:43:04.000+0000",
                "rating": 5,
                "text": "must be simple because I can use it no problem!\n",
                "sentiment": "NEUTRAL",
                "stars": "*****",
                "sentimentEnum": "NEUTRAL",
                "_links": {
                    "self": {
                        "href": "https://feedback-analysis-grp-app.herokuapp.com/feedback/d1df6785-b82b-43fd-950a-20771bcf6dcd"
                    },
                    "feedback": {
                        "href": "https://feedback-analysis-grp-app.herokuapp.com/feedback?dashboardId=e99f1a5e-5666-4f35-a08b-190aeeb2d0db"
                    }
                }
            }
        ]
    }
}
```

* Possible errors:
   * Code 400, parameters not present


## Count Feedback
`/feedback/count`

* Usage: count feedback
* Method: Get
* Query string:
`/feedback/count?dashboardId=...`

* Response body:

```
{
    "count": 2
}
```

* Possible errors:
   * Code 204, unable to serialize feedback count


## Count Sentiments
`/feedback/sentiments/count`

* Usage: get the number of sentiments separately
* Method: Get
* Query string:
`/feedback/sentiments/count?dashboardId=...`

* Response body:

```
{"NEUTRAL":11,"NEGATIVE":6,"POSITIVE":7}
```

* Possible error:
   * Code 204, unable to serialize sentiment counts


## Count Star Ratings
`/feedback/rating/count`

* Usage: get numbers of star ratings
* Method: Get
* Query string:
`/feedback/rating/count?dashboardId=...`

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
* Query string:
`/feedback/rating/average?dashboardId=...`

* Response body:

```
{"average":4.5}
```

* Possible error:
   * Code 204, unable to serialize average rating


## Count Negative Rating Per Day
`/feedback/rating/negativeperday`

* Usage: get number of negative ratings per day
* Method: Get
* Query string:
`/feedback/rating/negativeperday?dashboardId=...`

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
* Query string:
`/feedback/commonphrases?dashboardId=...`

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
