angular.module('cdiApp', ['ui.router'])

    .config(function($stateProvider) {

        $stateProvider
            .state('cdi', {
                url: "/cdi",
                templateUrl: "resources/app/commons/message.html",
                controller: 'cdiMessageController'
            });
    })
    .controller('cdiMessageController', ['$scope', 'messageService', '$log', function($scope, messageService, $log){

        $scope.logs = [];

        $scope.sendMessage = function(message) {

            messageService.openSocket()
            .then(function(socket) {

                    log("Before send message: " + message);
                    socket.send(angular.toJson({message: message}));
                    log("After send message: " + message);

                    socket.onmessage = function(event) {
                        $log.log(event);
                        $scope.$apply(function() {
                            var response = angular.fromJson(event.data);
                            log(response.message + ', session id: ' + response.sessionId);
                        });
                        if (event.data.indexOf('Echo') != -1) {
                            socket.close(1000, 'Thank you');
                        }
                    }

            }, function(event) {
                alert("Unable to open socket :(");
            });

            function log(message) {
                $scope.logs.push({
                    date: new Date(),
                    message: message
                });
            }
        };

}]);