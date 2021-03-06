sConsoleApp.controller( 'QBMController', function( $scope, $http ) {
	
	$scope.alerts = [] ;
	$scope.navBarTitle = "" ;
	$scope.interactingWithServer = false ;
	$scope.qbmMasterData = null ;
	
	$scope.lastUsedSearchCriteria = null ;
	$scope.searchCriteria = {
            selectedStandard : 7,
			selectedSubjects : [],
			selectedTopics : [],
			selectedBooks : [],
			selectedQuestionTypes : [],
			showOnlyUnsynched : true,
			excludeAttempted : true,
			searchText : "",
			testConfigId : -1
		} ;
    
	$scope.addErrorAlert = function( msgString ) {
	    $scope.alerts.push( { type: 'danger', msg: msgString } ) ;
	} ;
	
	$scope.dismissAlert = function( index ) {
		$scope.alerts.splice( index, 1 ) ;
	}

    $scope.loadQBMMasterData = function( callback ) {
    	
    	if( $scope.qbmMasterData != null ) return ;
        
        console.log( "Loading QBM master data from server." ) ;
        
        $scope.interactingWithServer = true ;
        $http.get( '/QBMMasterData' )
        .then( 
                function( response ){
                    console.log( "QBM master data received." ) ;
                    console.log( response ) ;
                    $scope.qbmMasterData = response.data ;
                    callback() ;
                }, 
                function( error ){
                    console.log( "Error getting QBM master data." ) ;
                    console.log( error ) ;
                    $scope.addErrorAlert( "Could not load master data." ) ;
                }
        )
        .finally(function() {
            $scope.interactingWithServer = false ;
        }) ;
    }
} ) ;