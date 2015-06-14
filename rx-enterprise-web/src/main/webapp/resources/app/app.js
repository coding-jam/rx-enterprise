var app = angular.module('starterApp', ['ngMaterial', 'ui.router', 'cdiApp', 'rxApp', 'appServices', 'appControllers']);

app.config(function($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.otherwise("/cdi");
});

app.controller('appController', ['$scope', '$mdSidenav', '$state', function($scope, $mdSidenav, $state){
    $scope.toggleSidenav = function(menuId) {
        $mdSidenav(menuId).toggle();
    };

    $scope.menuItems = $state.get().filter(function(element) {
        return !element.abstract;
    });

}]);