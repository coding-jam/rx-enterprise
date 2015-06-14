angular.module('rxApp', ['ui.router'])

    .config(function($stateProvider) {

        $stateProvider
            .state('rx', {
                url: "/rx",
                templateUrl: "resources/app/commons/message.html",
                controller: 'messageController',
                data: {
                    socket: 'rx',
                    title: 'RxJava + ManagedExecutors'
                }
            });
    });