.PHONY: up dev down reset logs rebuild dev-booking

up:
	docker compose up -d

dev:
	docker compose -f docker-compose.yml -f docker-compose.dev.yml up -d

down:
	docker compose down

reset:
	docker compose down -v && docker compose up -d

logs:
	docker compose logs -f $(S)

rebuild:
	docker compose up -d --build $(S)

dev-booking:
	docker compose -f docker-compose.yml -f docker-compose.dev.yml -f docker-compose.local-booking.yml up -d --scale booking-service=0