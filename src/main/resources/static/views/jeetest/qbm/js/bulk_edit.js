function BulkQEntry( entry ) {
	this.qref            = entry.qref ;
	this.questionType    = entry.questionType ;
	this.ansText         = entry.ansText ;
	this.difficultyLevel = entry.difficultyLevel ;
	this.projTime        = entry.projTime ;
	this.saved           = entry.saved ;
	this.imgNames        = entry.imgNames ;
	this.imgPaths        = entry.imgPaths ;
	this.hidden          = false ;
	this.topic           = entry.topic ;
	this.selected        = false ;
	this.preambleText    = "" ;
	
	this.mmtOptions = [] ;
	this.showMMTOptionsEditor = false ;
	
	this.nextEntry = null ;
	this.prevEntry = null ;
}

sConsoleApp.controller( 'BulkEditController', 
		                function( $scope, $http, $sce, $routeParams, $window, $timeout ) {
	
	var editHelper = new EditHelper() ;
	
	$scope.$parent.navBarTitle = "Create Questions in Bulk" ;
	$scope.validationErrors = [] ;
	$scope.baseInput = {
		subjectName : "IIT - Maths",
        standard : 0,
		topic : null,
		book : null,
		baseQRef : ""
	} ;
	
	$scope.entries = [] ;
	$scope.hideSaved = true ;
	$scope.preambleText = null ;
	
	// --- [START] Controller initialization
	
	// First load the drop down master data from the server. The drop down
	// master data will consist of subjects, topics, books etc.
	$scope.$parent.loadQBMMasterData( function() {
        $scope.baseInput.standard = $scope.$parent.qbmMasterData.standardNames[0] ;
    }) ;
	
	// --- [END] Controller initialization
	
	// --- [START] Scope functions
	$scope.applyBaseCriteria = function() {
		$scope.validationErrors.length = 0 ;
		validateBaseCriteria() ;
		
		if( $scope.validationErrors.length > 0 ) {
			$( '#validationErrorsDialog' ).modal( 'show' ) ;
		}
		else {
			getBulkQuestionMetaData() ;
		}
	}
	
	$scope.closeValidationErrorsDialog = function() {
		$scope.validationErrors.length = 0 ;
		$( '#validationErrorsDialog' ).modal( 'hide' ) ;
	}
	
	$scope.saveAll = function() {
	    if( $scope.entries.length > 0 ) {
	        $scope.saveEntry( $scope.entries[0], true ) ;
	    }
	}
	
	$scope.saveEntry = function( entry, saveLinkedEntry ) {
		
		if( entry == null ) return ;
		
		if( entry.hidden || entry.saved ) {
			$scope.saveEntry( entry.nextEntry, true ) ;
		}
		else {
            $scope.validationErrors.length = 0 ;
            if( editHelper.isAnswerSemanticallyValid( entry.ansText, entry.questionType, 
                                                      $scope.validationErrors ) ) {
                saveEntryOnServer( entry, saveLinkedEntry ) ;
            }
            else {
                for( var i=0; i<$scope.validationErrors.length; i++ ) {
                    $scope.validationErrors[i] = entry.qref + " : " + $scope.validationErrors[i] ;
                }
                $( '#validationErrorsDialog' ).modal( 'show' ) ;
            }
        }
	}
	
	$scope.deleteBulkEntryMetaData = function( entryIndex ) {
		
    	console.log( "Deleting bulk entry meta data." ) ;

    	var entry = $scope.entries[ entryIndex ] ;
    	
        $scope.$parent.interactingWithServer = true ;
        $http.post( '/DeleteTestQuestionImages', entry.imgPaths )
        .then( 
            function( response ){
                console.log( "Successfully deleted question images." ) ;
                $scope.entries.splice( entryIndex, 1 ) ;
            }, 
            function( error ){
                console.log( "Error deleting question images." + error ) ;
                var errMsg = "Server error." ;
                $scope.$parent.addErrorAlert( "Could not delete question images. " + errMsg ) ;
            }
        )
        .finally(function() {
            $scope.$parent.interactingWithServer = false ;
        }) ;
	}
	
	$scope.questionTypeChanged = function( entry ) {
		var nextEntry = entry.nextEntry ;
		var projTime = 120 ;
		
		if( $scope.baseInput.subjectName == "IIT - Chemistry" ) {
			if( entry.questionType == 'MCA' ) {
				projTime = 180 ;
			}
			else if ( entry.questionType == 'NT' ) {
				projTime = 180 ;
			}
		}
		else {
			if( entry.questionType == 'MCA' ) {
				projTime = 180 ;
			}
			else if ( entry.questionType == 'NT' ) {
				projTime = 240 ;
			}
		}
		
		entry.projTime = projTime ;
		while( nextEntry != null ) {
			nextEntry.questionType = entry.questionType ;
			nextEntry.projTime = projTime ;
			nextEntry = nextEntry.nextEntry ;
		}
	}
	
	$scope.answerEntered = function( entry ) {
	    if( entry.ansText.indexOf( ',' ) != -1 ) {
	        entry.questionType = 'MCA' ;
	        entry.projTime = 240 ;
	    }
	    
	    if( entry.ansText.includes( '-' ) && 
	        entry.ansText.indexOf( '-' ) != 0 ) {
	        
	        var parts = entry.ansText.split( '-' ) ;
	        entry.ansText = parts[0] ;
	        if( parts.length > 1 ) {
	            entry.difficultyLevel = parseInt( parts[1] ) ;
	        }
	        if( parts.length > 2 ) {
	            entry.projTime = parseInt( parts[2] ) ;
	        }
	    }
	}
	
	$scope.hideEntry = function( entry ) {
		entry.hidden = true ;
	}
	
	$scope.showMMTEditor = function( entry ) {
	    entry.showMMTOptionsEditor = true ;
	    $scope.$broadcast( 'mmtEditorIsBeingShown', entry ) ;
	}
	
	$scope.showPreambleDialog = function() {
        $( '#preambleDialog' ).modal( 'show' ) ;
    }
    
    $scope.hidePreambleDialog = function() {
        $( '#preambleDialog' ).modal( 'hide' ) ;
    }
    
    $scope.applyPreamble = function() {
        console.log( "Preamble = " + $scope.preambleText ) ;
        for( var i=0; i<$scope.entries.length; i++ ) {
            var entry = $scope.entries[i] ;
            if( !entry.hidden && !entry.saved ) {
                if( entry.selected ) {
                    entry.preambleText = $scope.preambleText ;
                }
            }
        }
        $scope.hidePreambleDialog() ;
    }
    
    $scope.entrySelected = function( entry ) {
        
        var prevEntry = entry.prevEntry ;
        while( prevEntry != null ) {
            prevEntry.selected = entry.selected ;
            prevEntry = prevEntry.prevEntry ;
        }
    }
	
	// --- [START] Internal functions
	function validateBaseCriteria() {
		
		if( $scope.baseInput.subjectName.trim() == "" ) {
			$scope.validationErrors.push( "Subject name should be specified." ) ;
		}
        
        if( $scope.baseInput.standard == 0 ) {
            $scope.validationErrors.push( "Standard should be specified." ) ;
        }
		
		if( $scope.baseInput.book == null ) {
			$scope.validationErrors.push( "Book should be specified." ) ;
		}
	}

    function getBulkQuestionMetaData() {
        
        var topicId = null ;
        if( $scope.baseInput.topic != null ) {
            topicId = $scope.baseInput.topic.id ;
        }
    	
        $scope.interactingWithServer = true ;
        $http.get( '/BulkQuestionsEntryMeta', {
            // NOTE: We are not passing the standard explicitly because it
            //       can be derived from the book
        	params:{
        		subjectName : $scope.baseInput.subjectName,
        		topicId : topicId,
        		bookId : $scope.baseInput.book.id,
        		baseQRef : $scope.baseInput.baseQRef
        	}
        } )
        .then( 
            function( response ){
                console.log( "Bulk question entry meta data received." ) ;
                console.log( response ) ;
                $scope.entries.length = 0 ;
                var lastEntry = null ;
                for( var i=0; i<response.data.length; i++ ) {
                	var newEntry = new BulkQEntry( response.data[i] ) ;
                	newEntry.prevEntry = lastEntry ;
                	if( lastEntry != null ) {
                		lastEntry.nextEntry = newEntry ;
                	}
                    $scope.entries.push( newEntry ) ;
                    lastEntry = newEntry ;
                }
            }, 
            function( error ){
                console.log( "Server error" ) ;
                console.log( error ) ;
                $scope.addErrorAlert( "Could not load bulk entry meta data." ) ;
            }
        )
        .finally(function() {
            $scope.interactingWithServer = false ;
        }) ;
    }
    
    function saveEntryOnServer( entry, saveLinkedEntry ) {
    	
    	console.log( "Saving entrh on server." ) ;
    	
    	var qText = "" ;
    	var tgtExam = ( entry.questionType == "MCA" || 
    	                entry.questionType == "LCT" || 
    	                entry.questionType == "MMT" ) ? "ADV" : "MAIN" ;
    	                
    	if( entry.preambleText != null || entry.preambleText != "" ) {
            qText = "<div class='q-preamble'>" + entry.preambleText + "</div>" ;
        }
    	
    	for( var i=0; i<entry.imgPaths.length; i++ ) {
    		qText += "{{@img " + entry.imgPaths[i] + "}}" ;
    		if( i < entry.imgPaths.length - 1 ) {
    			qText += "<br/>" ;
    		}
    	}
    	
    	if( entry.questionType == "MMT" && entry.mmtOptions.length > 0 ) {
    	    qText += "\n" ;
            qText += "<br/>\n" ;
            qText += "<ol type='A'>\n" ;
            for( var i=0; i<entry.mmtOptions.length; i++ ) {
                qText += "\t<li>)&nbsp;" + entry.mmtOptions[i] + "</li>\n" ;  
            }
            qText += "</ol>\n" ;
            console.log( qText ) ;
    	}
    	
        $scope.$parent.interactingWithServer = true ;
        $http.post( '/TestQuestion', {
            subject               : {
            	name : $scope.baseInput.subjectName,
            },
            topic                 : entry.topic,
            book                  : $scope.baseInput.book,
            targetExam            : tgtExam,
            questionType          : entry.questionType,
            questionRef           : entry.qref,
            difficultyLevel       : entry.difficultyLevel,
            projectedSolveTime    : entry.projTime,
            questionText          : qText,
            questionFormattedText : null,
            answerText            : entry.ansText,
            synched               : false,
            attempted             : false,
            creationTime          : null,
            lastUpdateTime        : null
        })
        .then( 
            function( response ){
                console.log( "Successfully saved question." ) ;
                entry.saved = true ;
                entry.hidden = true ;
                if( saveLinkedEntry && entry.nextEntry != null ) {
                    $scope.saveEntry( entry.nextEntry, true ) ;
                } 
            }, 
            function( error ){
                console.log( "Error saving question on server." + error ) ;
                var errMsg = "Server error." ;
                if( error.status == 412 ) {
                	errMsg = "Duplicate question found." ;
                }
                $scope.$parent.addErrorAlert( "Could not save question. " + errMsg ) ;
            }
        )
        .finally(function() {
            $scope.$parent.interactingWithServer = false ;
        }) ;
    }
} ) ;