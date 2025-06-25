<?php

namespace App\Command;

use Hyperf\Command\Command as HyperfCommand;
use App\Service\Distance\AddressQueueService;
use Hyperf\Command\Annotation\Command;
use Psr\Container\ContainerInterface;
use Psr\EventDispatcher\EventDispatcherInterface;

/**
 * @Command
 */
class QueueAddressCommand extends HyperfCommand
{
    /**
     * @var ContainerInterface
     */
    protected $container;
    private $addressGeoQueueName = 'address-geo';

    protected $name = 'queue:address-geo';

    public function __construct(ContainerInterface $container, EventDispatcherInterface $eventDispatcher)
    {
        $this->container = $container;
        $this->eventDispatcher = $eventDispatcher;

        parent::__construct();
    }

    public function configure()
    {
        parent::configure();
        $this->setDescription('解析收货地址信息坐标，并计算距离仓库的距离');
    }

    public function handle()
    {
        $queueCustomer = $this->container->get(AddressQueueService::class);
        if (!empty($queueCustomer)) {
            $queueCustomer->begin($this->addressGeoQueueName);
        }
    }
}