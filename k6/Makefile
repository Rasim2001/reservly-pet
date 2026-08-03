BASE_URL ?= http://localhost:8080

.PHONY: rate-limit
rate-limit:
	@echo "Rate limit test on /api/auth/login..."
	k6 run -e BASE_URL=$(BASE_URL) rate-limit.js


AUTH_URL ?= http://localhost:8081

.PHONY: brute
brute:
	@echo "Brute force lockout test on /api/auth/login..."
	k6 run -e AUTH_URL=$(AUTH_URL) auth-brute.js