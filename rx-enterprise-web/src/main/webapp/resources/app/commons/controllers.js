angular.module('appControllers', ['ui.router'])

    .controller('messageController', ['$scope', 'messageService', '$log', '$state', function($scope, messageService, $log, $state){

        $scope.logs = [];

        $scope.title = $state.current.data.title;

        $scope.sendMessage = function(message) {

            messageService.openSocket($state.current.data.socket)
            .then(function(socket) {

                    log("client", "Before send message: " + message);
                    socket.send(angular.toJson({message: message}));
                    log("client", "After send message: " + message);

                    socket.onmessage = function(event) {
                        $log.log(event);
                        $scope.$apply(function() {
                            var response = angular.fromJson(event.data);
                            log('server', response.message + ', session id: ' + response.sessionId, response.status || "OK");

                            if (event.data.indexOf('Echo') != -1) {
                                log("client", 'Close code "1000" with reason "Thank you"');
                                socket.close(1000, 'Thank you');
                            }
                            if (response.status == 'ERROR') {
                                log("client", 'Close code "4999" with reason "Server error"');
                                socket.close(4999, 'Server error');
                            }
                        });
                    }

            }, function(event) {
                alert("Unable to open socket :(");
            });

            function log(type, message, status) {
                $scope.logs.push({
                    date: new Date(),
                    message: (type == 'client' ? 'Client log: ' : 'Server log: ') + message,
                    status: status || "OK",
                    type: type
                });
            }
        };

}]);