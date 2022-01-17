var questionAttemptMap = {} ;
    
function Activity( qNum ) {
	this.questionNum = qNum ;
	this.start = 0 ;
	this.duration = 0 ;
	this.transitionEvent = null ;
}

function GanttActivity( activity ) {
    
    var attempt = questionAttemptMap[activity.questionNum] ;
    var isAnsCorrect = attempt.isCorrect ? "" : "[X] " ;
    
	this.label = isAnsCorrect + activity.questionNum ;
	this.start = activity.start ;
	this.duration = activity.duration ;
	this.color = getColor() ;
	
	function getColor() {
		
		var type = activity.transitionEvent ;
		if( type == null ) {
			return "#B9D7FB" ;
		}
		else if( type == ClickStreamEvent.prototype.ANSWER_SAVE ) {
			return "#00FF00" ;
		}
		else if( type == ClickStreamEvent.prototype.ANSWER_SAVE_AND_MARK_REVIEW ) {
			return "#0000FF" ;
		}
		else if( type == ClickStreamEvent.prototype.ANSWER_CLEAR_RESPONSE ) {
			return "#FF0000" ;
		}
		else if( type == ClickStreamEvent.prototype.ANSWER_MARK_FOR_REVIEW ) {
			return "#CA8D36" ;
		}
		return "#000000" ;
	}
}

sConsoleApp.controller( 'TestAttemptTimeSequenceController', function( $scope, $http, $location, $routeParams ) {
    
    var questionIdNumMap = {} ;
    var questionActivityListMap = {} ;
    var lapMarkers = [] ;
    
    var graph = null ;
    var allGraphData = [] ;
    var currentGraphData = [] ;
    var graphTooltips = [] ;

	$scope.$parent.navBarTitle = "Test Attempt Time Sequence" ;
	$scope.testAttemptId = $routeParams.id ;
	$scope.questions = null ;
	$scope.questionAttempts = null ;
	$scope.clickEvents = null ;
	$scope.autoRefresh = false ;
	
	var lapColors = [
	    'rgba(51,255,0,0.4)',
	    'rgba(204,102,255,0.4)',
	    'rgba(0,0,255,0.4)',
	    'rgba(204,255,0,0.4)',
	    'rgba(102,102,255,0.4)',
	    'rgba(255,51,102,0.4)',
	    'rgba(204,0,0,0.4)'
	] ;
	
	// -----------------------------------------------------------------------
	// --- [START] Controller initialization ---------------------------------
	
	fetchTestClickEventDetails( $scope.testAttemptId ) ;
		
	// --- [END] Controller initialization -----------------------------------
	
	// -----------------------------------------------------------------------
	// --- [START] Scope functions -------------------------------------------
	
    $scope.returnToAttemptIndex = function() {
    	$location.path( "/" ) ;  
    }
    
    $scope.autoRefreshChanged = function() {
    	if( $scope.autoRefresh ) {
    		autoRefreshGraph() ;
    	}
    }
    
	// --- [END] Scope functions
	
	// -----------------------------------------------------------------------
	// --- [START] Local functions -------------------------------------------
    
    function autoRefreshGraph() {
    	fetchTestClickEventDetails( $scope.testAttemptId ) ;
    }
    
    function getQNum( index ) {
    	return "Q-" + (index+1) ;
    }
    
    function fetchTestClickEventDetails( testAttemptId ) {
    	
    	console.log( "Fetching click events for test attempt : " + testAttemptId ) ;
        $scope.$parent.interactingWithServer = true ;
        $http.get( "/ClickStreamEvent/TestAttempt/" + testAttemptId )
        .then( 
            function( response ){
                console.log( response ) ;
                $scope.clickEvents = response.data[0] ;
                $scope.questions = response.data[1] ;
                $scope.questionAttempts = response.data[2] ;
                
                createQuestionMaps() ;
                processClickStreamEvents( $scope.clickEvents ) ;
                setUpActivityGraph() ;
                
            	if( $scope.autoRefresh ) {
            		setTimeout( autoRefreshGraph, 5000 ) ;
            	}
            }, 
            function( error ){
                console.log( "Error getting test attempt click events." ) ;
                console.log( error ) ;
                $scope.$parent.addErrorAlert( "Error getting Test attempt click events." ) ;
            }
        )
        .finally(function() {
            $scope.$parent.interactingWithServer = false ;
        }) ;
    }
    
    function createQuestionMaps() {
        
        lapMarkers.length = 0 ;
        questionIdNumMap = {} ;
        questionAttemptMap = {} ;
        
    	for( var i=0; i<$scope.questions.length; i++ ) {
    		var question = $scope.questions[i] ;
    		var attempt  = $scope.questionAttempts[i] ;
    		
    		var qNum = getQNum( i ) ;
    		
    		questionIdNumMap[ question.id ] = qNum ;
    		questionAttemptMap[ qNum ] = attempt ;
    		questionActivityListMap[ qNum ] = [] ;
    	}
    }
    
    function processClickStreamEvents( clickEvents ) {
    	
    	var lastActivity = null ;
    	var currentLapStartTime = 0 ;
    	var currentLapEndTime = 0 ;
    	var currentLapIndex = -1 ;
    	
    	for( var i=0; i<clickEvents.length; i++ ) {
    		
    		var event = clickEvents[i] ;
    		var eventType = event.eventId ;
    		
    		if( eventType == ClickStreamEvent.prototype.QUESTION_VISITED ) {
    			
    			var qNum = questionIdNumMap[ event.payload ] ;
    			
    			var activity = new Activity( qNum ) ;
    			activity.start = Math.floor( event.timeMarker/1000 ) ;
    			
    			questionActivityListMap[ qNum ].push( activity ) ;
    			
    			if( lastActivity != null ) {
    				lastActivity.duration = activity.start - lastActivity.start ;
    			}
    			
    			lastActivity = activity ;
    		}
    		else if( eventType == ClickStreamEvent.prototype.ANSWER_SAVE ||
    				 eventType == ClickStreamEvent.prototype.ANSWER_SAVE_AND_MARK_REVIEW || 
    				 eventType == ClickStreamEvent.prototype.ANSWER_CLEAR_RESPONSE  || 
    				 eventType == ClickStreamEvent.prototype.ANSWER_MARK_FOR_REVIEW ) {
    			
    			var qNum = questionIdNumMap[ event.payload ] ;
    			var activities = questionActivityListMap[ qNum ] ;
    			
    			if( activities.length > 0 ) {
    				var activity = activities[ activities.length - 1 ] ;
    				activity.transitionEvent = eventType ;
    			}
    		}
    		else if( eventType == ClickStreamEvent.prototype.LAP_START ) {
    		    currentLapIndex++ ;
    		    currentLapStartTime = Math.floor( event.timeMarker/1000 ) ;
    		}
            else if( eventType == ClickStreamEvent.prototype.LAP_END ) {
                currentLapEndTime = Math.floor( event.timeMarker/1000 ) ;
                lapMarkers.push( [
                    currentLapStartTime,
                    (currentLapEndTime - currentLapStartTime),
                    lapColors[ currentLapIndex ]
                ]) ;
            }
    	}
    }
    
    function setUpActivityGraph() {
        
        graphTooltips.length = 0 ;
        allGraphData.length = 0 ;
        currentGraphData.length = 0 ;
        
        setUpRGraph() ;
        
        for( var i=0; i<$scope.questions.length; i++ ) {
            var attempt = $scope.questionAttempts[i] ;
            var qNum = getQNum( i ) ;
            
            var activities = questionActivityListMap[ qNum ] ;
            var ganttActivities = [] ;
            
            // If this question has not been visited, no click stream activity
            // would have been generated. This would cause RGraph to throw
            // and error. We inject a dummy activity of duration 0.
            if( activities.length == 0 ) {
                var activity = new Activity( qNum ) ;
                activities.push( activity ) ;
            }
            
            for( var j=0; j<activities.length; j++ ) {
                var activity = activities[j] ;
                ganttActivities.push( new GanttActivity( activity ) ) ;
                
                var tooltip = qNum ;
                tooltip += "<br>" + activity.duration + " sec" ;
                if( activity.transitionEvent != null ) {
                    tooltip += "<br>" + activity.transitionEvent ;
                }
                if( !attempt.isCorrect ) {
                    tooltip += "<br> Incorrect answer" ;
                    tooltip += "<br> Root cause - " + attempt.rootCause ;
                }
                else {
                    tooltip += "<br> Correct answer" ;
                }
                tooltip += "<br> Total time = " + attempt.timeSpent ;
                
                graphTooltips.push( tooltip ) ;
            }
            
            allGraphData.push( ganttActivities ) ;
        }
        
        animateGantt() ;
    }
    
    function setUpRGraph() {
        
        var maxTime = ($scope.clickEvents[ $scope.clickEvents.length - 1 ].timeMarker) / 1000 ;
        maxTime = Math.floor( maxTime ) ;
        
        var canvas = document.getElementById( 'testAttemptGantt' ) ;
        canvas.width = window.innerWidth ;
        canvas.height = window.innerHeight - 50 ;
        
        var xLabels = [] ;
        var xAxisDivs = Math.floor(maxTime/300) ;
        
        for( var i=0; i<xAxisDivs; i++ ) {
            xLabels.push( "" + (i+1)*5 ) ;
        }

        RGraph.reset( canvas ) ;
        
        graph = new RGraph.Gantt({
            id: 'testAttemptGantt',
            data: currentGraphData,
            options: {
                backgroundGrid : true,
                backgroundGridVlinesCount: xAxisDivs,
                xaxisLabels : xLabels,
                xaxisScaleMax: maxTime,
                yaxisLabelsSize: 10,
                tooltips: graphTooltips,
                tooltipsEvent : 'mousemove', 
                hmargin: 5,
                colorsDefault: 'green',
                vmargin: 1,
                backgroundVbars: lapMarkers            
            }
        }).draw() ;
    }
    
    function animateGantt() {
        
        if( allGraphData.length > 0 ) {
            currentGraphData.push( allGraphData.shift() ) ;
            RGraph.redraw() ;
            
            setTimeout( animateGantt, 1000 ) ;
        }        
    }    
    
    // --- [END] Local functions
} ) ;