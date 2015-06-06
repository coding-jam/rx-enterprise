var app = angular.module('starterApp', ['ngMaterial', 'ui.router', 'cdiApp', 'appServices']);

app.config(function($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.otherwise("/cdi");
});

app.controller('appController', ['$scope', '$mdSidenav', function($scope, $mdSidenav){
    $scope.toggleSidenav = function(menuId) {
        $mdSidenav(menuId).toggle();
    };

}]);