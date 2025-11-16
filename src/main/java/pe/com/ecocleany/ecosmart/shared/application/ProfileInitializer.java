package pe.com.ecocleany.ecosmart.shared.application;

import java.util.UUID;

/**
 * Shared interface to ensure that a Profile exists for a given IAM user.
 */
public interface ProfileInitializer {
    void ensureProfile(UUID userId);
}
