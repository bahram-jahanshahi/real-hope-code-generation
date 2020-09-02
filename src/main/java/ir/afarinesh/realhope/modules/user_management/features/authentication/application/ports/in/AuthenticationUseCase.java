package ir.afarinesh.realhope.modules.user_management.features.authentication.application.ports.in;

import ir.afarinesh.realhope.core.annotations.UseCase;
import ir.afarinesh.realhope.core.usecase.UseCaseFruit;
import ir.afarinesh.realhope.core.usecase.UseCasePlant;
import ir.afarinesh.realhope.modules.user_management.features.authentication.application.ports.in.commands.AuthenticationCommand;
import ir.afarinesh.realhope.modules.user_management.features.authentication.application.ports.in.exceptions.AuthenticationUseCaseException;
import ir.afarinesh.realhope.modules.user_management.features.authentication.application.ports.in.fruits.AuthenticationFruit;

@UseCase
public interface AuthenticationUseCase {

    UseCaseFruit<AuthenticationFruit> authenticate(UseCasePlant<AuthenticationCommand> command)
            throws AuthenticationUseCaseException;
}
